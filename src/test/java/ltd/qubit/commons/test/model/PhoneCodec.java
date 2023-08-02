////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import ltd.qubit.commons.util.codec.Codec;
import ltd.qubit.commons.util.codec.DecodingException;

import static ltd.qubit.commons.lang.StringUtils.isEmpty;
import static ltd.qubit.commons.lang.StringUtils.strip;

public class PhoneCodec implements Codec<Phone, String> {

  @Override
  public Phone decode(final String source) throws DecodingException {
    final String theSource = strip(source);
    if (isEmpty(theSource)) {
      return null;
    }
    final String[] parts = theSource.split(Phone.PART_SEPARATOR);
    final Phone result = new Phone();
    switch (parts.length) {
      case 1:
        result.setNumber(theSource);
        return result;
      case 2:
        if (parts[0].startsWith(Phone.COUNTRY_CODE_PREFIX)) {
          result.setCountryArea(parts[0].substring(Phone.COUNTRY_CODE_PREFIX.length()));
          result.setNumber(parts[1]);
        } else {
          result.setCityArea(parts[0]);
          result.setNumber(parts[1]);
        }
        return result;
      case 3:
        if (parts[0].startsWith(Phone.COUNTRY_CODE_PREFIX)) {
          result.setCountryArea(parts[0].substring(Phone.COUNTRY_CODE_PREFIX.length()));
          result.setCityArea(parts[1]);
          result.setNumber(parts[2]);
          return result;
        } else {
          throw new DecodingException("Invalid phone number format: " + theSource);
        }
      default:
        throw new DecodingException("Invalid phone number format: " + theSource);
    }
  }

  public Phone decodeWithoutThrow(final String source) {
    try {
      return decode(source);
    } catch (final DecodingException e) {
      return null;
    }
  }

  @Override
  public String encode(final Phone phone) {
    if (phone == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    if (! isEmpty(phone.getCountryArea())) {
      builder.append(Phone.COUNTRY_CODE_PREFIX)
             .append(phone.getCountryArea())
             .append(Phone.PART_SEPARATOR);
    }
    if (! isEmpty(phone.getCityArea())) {
      builder.append(phone.getCityArea())
             .append(Phone.PART_SEPARATOR);
    }
    builder.append(phone.getNumber());
    return builder.toString();
  }
}
