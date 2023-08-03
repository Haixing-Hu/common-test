////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.time.Instant;

import javax.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ltd.qubit.commons.annotation.Identifier;
import ltd.qubit.commons.annotation.Unique;
import ltd.qubit.commons.lang.Argument;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Assignment;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;
import ltd.qubit.commons.text.xml.jaxb.IsoInstantXmlAdapter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示第三方应用。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "app")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class App implements Identifiable, WithInfo, Stateful, Auditable,
    Assignable<App> {

  /**
   * 本系统所对应的 App 的代码。
   */
  public static final String SYSTEM_APP_CODE = "system";
  private static final long serialVersionUID = -4130818293179381259L;

  /**
   * 唯一标识，系统自动生成。
   */
  @XmlElement(name = "id")
  @Identifier
  private Long id;

  /**
   * 代码，全局不可重复，一旦设置不能更改。
   */
  @XmlElement(name = "code")
  @Unique
  @Size(min = 1, max = 64)
  private String code;

  /**
   * 名称，同一机构下不可重复。
   */
  @XmlElement(name = "name")
  @Size(min = 1, max = 256)
  private String name;

  /**
   * 所属类别基本信息。
   */
  @XmlElement(name = "category")
  @Nullable
  private Info category;

  /**
   * 状态。
   */
  @XmlElement(name = "state")
  @NotNull
  private State state;

  /**
   * 图标。
   */
  @XmlElement(name = "icon")
  @Nullable
  @Size(max = 512)
  private String icon;

  /**
   * 网址 URL。
   */
  @XmlElement(name = "url")
  @Nullable
  @Size(max = 512)
  private String url;

  /**
   * 描述。
   */
  @XmlElement(name = "description")
  @Nullable
  private String description;

  /**
   * 备注。
   */
  @XmlElement(name = "comment")
  @Nullable
  private String comment;

  /**
   * 安全秘钥，从数据库中读取出来的是秘钥加盐后的哈希值。
   */
  @XmlElement(name = "security-key")
  @Size(min = 1, max = 4096)
  private String securityKey;

  /**
   * 访问令牌。
   */
  @XmlElement(name = "token")
  @Nullable
  @Size(min = 1, max = 128)
  private String token;

  /**
   * 访问令牌创建时间。
   */
  @XmlElement(name = "token-create-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  @Nullable
  private Instant tokenCreateTime;

  /**
   * 访问令牌过期时间。
   */
  @XmlElement(name = "token-expired-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  @Nullable
  private Instant tokenExpiredTime;

  /**
   * 是否是预定义的数据。
   */
  @XmlElement(name = "predefined")
  private boolean predefined;

  /**
   * 创建时间。
   */
  @XmlElement(name = "create-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  private Instant createTime;

  /**
   * 最后一次修改时间。
   */
  @XmlElement(name = "modify-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  @Nullable
  private Instant modifyTime;

  /**
   * 删除时间。
   */
  @XmlElement(name = "delete-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  @Nullable
  private Instant deleteTime;

  public App() {
    // empty
  }

  public App(final App other) {
    assign(other);
  }

  @Override
  public void assign(final App other) {
    Argument.requireNonNull("other", other);
    id = other.id;
    code = other.code;
    name = other.name;
    category = Assignment.clone(other.category);
    state = other.state;
    icon = other.icon;
    url = other.url;
    description = other.description;
    comment = other.comment;
    securityKey = other.securityKey;
    token = other.token;
    tokenCreateTime = other.tokenCreateTime;
    tokenExpiredTime = other.tokenExpiredTime;
    predefined = other.predefined;
    createTime = other.createTime;
    modifyTime = other.modifyTime;
    deleteTime = other.deleteTime;
  }

  @Override
  public App clone() {
    return new App(this);
  }

  public final Long getId() {
    return id;
  }

  public final void setId(final Long id) {
    this.id = id;
  }

  public final String getCode() {
    return code;
  }

  public final void setCode(final String code) {
    this.code = code;
  }

  public final String getName() {
    return name;
  }

  public final void setName(final String name) {
    this.name = name;
  }

  @Nullable
  public final Info getCategory() {
    return category;
  }

  public final void setCategory(@Nullable final Info category) {
    this.category = category;
  }

  public final State getState() {
    return state;
  }

  public final void setState(final State state) {
    this.state = state;
  }

  @Nullable
  public final String getIcon() {
    return icon;
  }

  public final void setIcon(@Nullable final String icon) {
    this.icon = icon;
  }

  @Nullable
  public final String getUrl() {
    return url;
  }

  public final void setUrl(@Nullable final String url) {
    this.url = url;
  }

  @Nullable
  public final String getDescription() {
    return description;
  }

  public final void setDescription(@Nullable final String description) {
    this.description = description;
  }

  @Nullable
  public final String getComment() {
    return comment;
  }

  public final void setComment(@Nullable final String comment) {
    this.comment = comment;
  }

  public final String getSecurityKey() {
    return securityKey;
  }

  public final void setSecurityKey(final String securityKey) {
    this.securityKey = securityKey;
  }

  @Nullable
  public final String getToken() {
    return token;
  }

  public final void setToken(@Nullable final String token) {
    this.token = token;
  }

  @Nullable
  public final Instant getTokenCreateTime() {
    return tokenCreateTime;
  }

  public final void setTokenCreateTime(@Nullable final Instant tokenCreateTime) {
    this.tokenCreateTime = tokenCreateTime;
  }

  @Nullable
  public final Instant getTokenExpiredTime() {
    return tokenExpiredTime;
  }

  public final void setTokenExpiredTime(@Nullable final Instant tokenExpiredTime) {
    this.tokenExpiredTime = tokenExpiredTime;
  }

  public final boolean isPredefined() {
    return predefined;
  }

  public final void setPredefined(final boolean predefined) {
    this.predefined = predefined;
  }

  public final Instant getCreateTime() {
    return createTime;
  }

  public final void setCreateTime(final Instant createTime) {
    this.createTime = createTime;
  }

  @Nullable
  public final Instant getModifyTime() {
    return modifyTime;
  }

  public final void setModifyTime(@Nullable final Instant modifyTime) {
    this.modifyTime = modifyTime;
  }

  @Nullable
  public final Instant getDeleteTime() {
    return deleteTime;
  }

  public final void setDeleteTime(@Nullable final Instant deleteTime) {
    this.deleteTime = deleteTime;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final App other = (App) o;
    return Equality.equals(id, other.id)
        && Equality.equals(code, other.code)
        && Equality.equals(name, other.name)
        && Equality.equals(category, other.category)
        && Equality.equals(state, other.state)
        && Equality.equals(icon, other.icon)
        && Equality.equals(url, other.url)
        && Equality.equals(description, other.description)
        && Equality.equals(comment, other.comment)
        && Equality.equals(securityKey, other.securityKey)
        && Equality.equals(token, other.token)
        && Equality.equals(tokenCreateTime, other.tokenCreateTime)
        && Equality.equals(tokenExpiredTime, other.tokenExpiredTime)
        && Equality.equals(predefined, other.predefined)
        && Equality.equals(createTime, other.createTime)
        && Equality.equals(modifyTime, other.modifyTime)
        && Equality.equals(deleteTime, other.deleteTime);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, category);
    result = Hash.combine(result, multiplier, state);
    result = Hash.combine(result, multiplier, icon);
    result = Hash.combine(result, multiplier, url);
    result = Hash.combine(result, multiplier, description);
    result = Hash.combine(result, multiplier, comment);
    result = Hash.combine(result, multiplier, securityKey);
    result = Hash.combine(result, multiplier, token);
    result = Hash.combine(result, multiplier, tokenCreateTime);
    result = Hash.combine(result, multiplier, tokenExpiredTime);
    result = Hash.combine(result, multiplier, predefined);
    result = Hash.combine(result, multiplier, createTime);
    result = Hash.combine(result, multiplier, modifyTime);
    result = Hash.combine(result, multiplier, deleteTime);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("code", code)
        .append("name", name)
        .append("category", category)
        .append("state", state)
        .append("icon", icon)
        .append("url", url)
        .append("description", description)
        .append("comment", comment)
        .append("securityKey", securityKey)
        .append("token", token)
        .append("tokenCreateTime", tokenCreateTime)
        .append("tokenExpiredTime", tokenExpiredTime)
        .append("predefined", predefined)
        .append("createTime", createTime)
        .append("modifyTime", modifyTime)
        .append("deleteTime", deleteTime)
        .toString();
  }
}
