/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2.converter.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import javax.annotation.Nullable;

final class JacksonRequestBodyConverter<T> implements Converter<T, RequestBody> {
  private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");

  @Nullable
  private ObjectWriter adapter;
  private final Type type;
  private final ObjectMapper mapper;

  JacksonRequestBodyConverter(Type type, ObjectMapper mapper) {
    this.type = type;
    this.mapper = mapper;
  }

  @Override
  public RequestBody convert(T value) throws IOException {
    if (adapter == null) {
      JavaType javaType = mapper.getTypeFactory().constructType(type);
      adapter = mapper.writerFor(javaType);
    }

    byte[] bytes = adapter.writeValueAsBytes(value);
    return RequestBody.create(MEDIA_TYPE, bytes);
  }
}
