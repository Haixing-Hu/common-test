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
import ltd.qubit.commons.util.codec.DecodingException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

public class PhoneJsonDeserializer extends StdDeserializer<Phone> {

  private static final long serialVersionUID = -5066519475467960647L;

  private final Codec<Phone, String> codec;

  public PhoneJsonDeserializer() {
    this(new PhoneCodec());
  }

  public PhoneJsonDeserializer(final Codec<Phone, String> codec) {
    super(Phone.class);
    this.codec = requireNonNull("codec", codec);
  }

  @Override
  public Phone deserialize(final JsonParser parser,
      final DeserializationContext context)
      throws IOException {
    final String str = parser.getText();
    try {
      return codec.decode(str);
    } catch (final DecodingException e) {
      throw new IOException(e);
    }
  }
}
