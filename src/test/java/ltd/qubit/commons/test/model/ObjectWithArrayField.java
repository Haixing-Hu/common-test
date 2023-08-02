////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "object-with-array-field")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectWithArrayField {

  @Size(min = 1, max = 10)
  private String[] stringArray;

  @Size(min = 1, max = 5)
  private boolean[] booleanArray;

  @Size(min = 1, max = 5)
  private char[] charArray;

  @Size(min = 1, max = 5)
  private byte[] byteArray;

  @Size(min = 1, max = 5)
  private short[] shortArray;

  @Size(min = 1, max = 5)
  private int[] intArray;

  @Size(min = 1, max = 5)
  private long[] longArray;

  @Size(min = 1, max = 5)
  private float[] floatArray;

  @Size(min = 1, max = 5)
  private double[] doubleArray;

  public final String[] getStringArray() {
    return stringArray;
  }

  public final ObjectWithArrayField setStringArray(
      final String[] stringArray) {
    this.stringArray = stringArray;
    return this;
  }

  public final boolean[] getBooleanArray() {
    return booleanArray;
  }

  public final ObjectWithArrayField setBooleanArray(
      final boolean[] booleanArray) {
    this.booleanArray = booleanArray;
    return this;
  }

  public final char[] getCharArray() {
    return charArray;
  }

  public final ObjectWithArrayField setCharArray(final char[] charArray) {
    this.charArray = charArray;
    return this;
  }

  public final byte[] getByteArray() {
    return byteArray;
  }

  public final ObjectWithArrayField setByteArray(final byte[] byteArray) {
    this.byteArray = byteArray;
    return this;
  }

  public final short[] getShortArray() {
    return shortArray;
  }

  public final ObjectWithArrayField setShortArray(final short[] shortArray) {
    this.shortArray = shortArray;
    return this;
  }

  public final int[] getIntArray() {
    return intArray;
  }

  public final ObjectWithArrayField setIntArray(final int[] intArray) {
    this.intArray = intArray;
    return this;
  }

  public final long[] getLongArray() {
    return longArray;
  }

  public final ObjectWithArrayField setLongArray(final long[] longArray) {
    this.longArray = longArray;
    return this;
  }

  public final float[] getFloatArray() {
    return floatArray;
  }

  public final ObjectWithArrayField setFloatArray(final float[] floatArray) {
    this.floatArray = floatArray;
    return this;
  }

  public final double[] getDoubleArray() {
    return doubleArray;
  }

  public final ObjectWithArrayField setDoubleArray(final double[] doubleArray) {
    this.doubleArray = doubleArray;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final ObjectWithArrayField other = (ObjectWithArrayField) o;
    return Equality.equals(stringArray, other.stringArray)
        && Equality.equals(booleanArray, other.booleanArray)
        && Equality.equals(charArray, other.charArray)
        && Equality.equals(byteArray, other.byteArray)
        && Equality.equals(shortArray, other.shortArray)
        && Equality.equals(intArray, other.intArray)
        && Equality.equals(longArray, other.longArray)
        && Equality.equals(floatArray, other.floatArray)
        && Equality.equals(doubleArray, other.doubleArray);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, stringArray);
    result = Hash.combine(result, multiplier, booleanArray);
    result = Hash.combine(result, multiplier, charArray);
    result = Hash.combine(result, multiplier, byteArray);
    result = Hash.combine(result, multiplier, shortArray);
    result = Hash.combine(result, multiplier, intArray);
    result = Hash.combine(result, multiplier, longArray);
    result = Hash.combine(result, multiplier, floatArray);
    result = Hash.combine(result, multiplier, doubleArray);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("stringArray", stringArray)
        .append("booleanArray", booleanArray)
        .append("charArray", charArray)
        .append("byteArray", byteArray)
        .append("shortArray", shortArray)
        .append("intArray", intArray)
        .append("longArray", longArray)
        .append("floatArray", floatArray)
        .append("doubleArray", doubleArray)
        .toString();
  }
}
