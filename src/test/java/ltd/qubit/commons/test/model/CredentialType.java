////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.EnumUtils;

import static ltd.qubit.commons.lang.EnumUtils.registerLocalizedNames;

/**
 * 此枚举表示证件类型。
 *
 * <p><b>参考资料：</b>
 * <ul>
 *   <li>《WS 364.3-2011 卫生信息数据元值域代码 第3部分：人口学及社会经济学特征》
 *   CV02.01.101 身份证件类别代码</li>
 *   <li><a href="http://meta.omaha.org.cn/range/get?code=CV02.01.101">身份证件
 *   类别代码</a></li>
 * </ul>
 *
 * @author 胡海星
 */
@XmlRootElement(name = "credential-type")
public enum CredentialType {

  /**
   * 身份证。
   */
  IDENTITY_CARD("01"),

  /**
   * 户口簿。
   */
  RESIDENCE_BOOKLET("02"),

  /**
   * 护照。
   */
  PASSPORT("03"),

  /**
   * 军官证。
   */
  OFFICER_CARD("04"),

  /**
   * 驾照。
   */
  DRIVING_CARD("05"),

  /**
   * 港澳居民来往内地通行证。
   */
  HONGKONG_MACAO_RETURN_PERMIT("06"),

  /**
   * 台湾居民来往大陆通行证。
   */
  TAIWAN_MACAO_RETURN_PERMIT("07"),

  /**
   * 港澳台居民来往大陆通行证（非《WS 364.3-2011》规定，自定义扩展，向前兼容，今后会删除）。
   */
  RETURN_PERMIT("20"),

  /**
   * 出生证明（非《WS 364.3-2011》规定，自定义扩展）。
   */
  BIRTH_CERTIFICATE("21"),

  /**
   * 社保卡（非《WS 364.3-2011》规定，自定义扩展）。
   */
  SOCIAL_SECURITY_CARD("31"),

  /**
   * 医保卡（非《WS 364.3-2011》规定，自定义扩展）。
   */
  MEDICARE_CARD("32"),

  /**
   * 工作证（非《WS 364.3-2011》规定，自定义扩展）。
   */
  EMPLOYEE_CARD("50"),

  /**
   * 执业资格证（非《WS 364.3-2011》规定，自定义扩展）。
   */
  PRACTISING_CERTIFICATE("51"),

  /**
   * 职称资格证（非《WS 364.3-2011》规定，自定义扩展）。
   */
  TITLE_CERTIFICATE("52"),

  /**
   * 营业执照（非《WS 364.3-2011》规定，自定义扩展，仅用于组织机构）。
   */
  BUSINESS_LICENSE("70"),

  /**
   * 组织机构代码（非《WS 364.3-2011》规定，自定义扩展，仅用于组织机构）。
   */
  ORGANIZATION_CODE("71"),

  /**
   * 其他。
   */
  OTHER("99");


  private final String code;

  CredentialType(final String code) {
    this.code = code;
  }

  public String code() {
    return code;
  }

  static {
    registerLocalizedNames(CredentialType.class, "i18n/credential-type");
  }

  public String getLocalizedName() {
    return getLocalizedName(Locale.getDefault());
  }

  public String getLocalizedName(final Locale locale) {
    return EnumUtils.getLocalizedName(locale, this);
  }

  public static CredentialType forCode(final String code) {
    return CODE_MAP.get(code);
  }

  private static final Map<String, CredentialType> CODE_MAP = new HashMap<>();
  static {
    for (final CredentialType e : values()) {
      CODE_MAP.put(e.code, e);
    }
  }

  public static Map<String, CredentialType> getNameMap(final Locale locale) {
    final Map<String, CredentialType> result = new HashMap<>();
    for (final CredentialType e : values()) {
      result.put(e.getLocalizedName(locale), e);
    }
    return result;
  }
}
