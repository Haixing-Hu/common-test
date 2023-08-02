////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.io.IOException;

import ltd.qubit.commons.util.codec.Codec;
import ltd.qubit.commons.util.codec.EncodingException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

public class PhoneJsonSerializer extends StdSerializer<Phone> {

  private static final long serialVersionUID = -6010753017429561078L;

  private final Codec<Phone, String> codec;

  public PhoneJsonSerializer() {
    this(new PhoneCodec());
  }

  public PhoneJsonSerializer(final Codec<Phone, String> codec) {
    super(Phone.class);
    this.codec = requireNonNull("codec", codec);
  }

  @Override
  public void serialize(final Phone phone, final JsonGenerator generator,
          final SerializerProvider serializerProvider) throws IOException {
    try {
      generator.writeString(codec.encode(phone));
    } catch (final EncodingException e) {
      throw new IOException(e);
    }
  }
}
