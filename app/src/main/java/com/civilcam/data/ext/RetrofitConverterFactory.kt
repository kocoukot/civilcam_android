package com.civilcam.data.ext

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.standartmedia.common.ext.castSafe
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Type
import java.nio.charset.Charset

class RetrofitConverterFactory(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> =
        GsonResponseBodyConverter(gson, gson.getAdapter(TypeToken.get(type)), annotations)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> =
        GsonRequestBodyConverter(gson, gson.getAdapter(TypeToken.get(type)))

    internal class GsonRequestBodyConverter<T>(
        private val gson: Gson,
        private val adapter: TypeAdapter<T>
    ) :
        Converter<T, RequestBody> {
        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return buffer.readByteString().toRequestBody(MEDIA_TYPE)
        }

        companion object {
            private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
            private val UTF_8 = Charset.forName("UTF-8")
        }
    }

    internal class GsonResponseBodyConverter<T>(
        private val gson: Gson,
        private val adapter: TypeAdapter<T>,
        private val annotations: Array<out Annotation>
    ) : Converter<ResponseBody, T> {
        private val annotationProcessStrategies = listOf(
            AnnotationProcessStrategy.ExtractObjectAnnotationProcessStrategy()
        )

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val jsonReader = gson.newJsonReader(value.charStream())
            annotationProcessStrategies
                .forEach { it.process(annotations, jsonReader) }
            return value.use {
                val result = adapter.read(jsonReader)
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
                result
            }
        }
    }

    interface AnnotationProcessStrategy {

        fun <T : Annotation> process(annotations: Array<T>, jsonReader: JsonReader): JsonReader

        class ExtractObjectAnnotationProcessStrategy : AnnotationProcessStrategy {

            override fun <T : Annotation> process(
                annotations: Array<T>,
                jsonReader: JsonReader
            ): JsonReader = annotations
                .find { it is ExtractObject }
                .castSafe<ExtractObject>()
                ?.let {
                    if (jsonReader.peek() == JsonToken.NAME && jsonReader.nextName() == it.fieldName) {
                        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
                            jsonReader.beginObject()
                        }
                    }
                    jsonReader
                }
                ?: jsonReader
        }
    }
}