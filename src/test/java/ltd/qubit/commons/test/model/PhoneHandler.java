////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ltd.qubit.commons.util.codec.DecodingException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MyBatis type handler for the {@link Phone} class.
 *
 * <p>It maps {@link Phone} &lt;-&gt; {@link String}
 *
 * @author Haixing Hu
 */
@MappedTypes(Phone.class)
public class PhoneHandler extends BaseTypeHandler<Phone> {

  private final Logger logger = LoggerFactory.getLogger(PhoneHandler.class);
  private final PhoneCodec codec = new PhoneCodec();

  @Override
  public void setNonNullParameter(final PreparedStatement ps, final int i,
          final Phone phone, final JdbcType jdbcType) throws SQLException {
    if (phone == null) {
      ps.setString(i, null);
    } else {
      ps.setString(i, codec.encode(phone));
    }
  }

  @Override
  public Phone getNullableResult(final ResultSet rs, final String columnName)
      throws SQLException {
    final String str = rs.getString(columnName);
    if (str != null) {
      try {
        return codec.decode(str);
      } catch (final DecodingException e) {
        logger.error("An error occurs: {}", e.getMessage(), e);
        throw new SQLException(e);
      }
    }
    return null;
  }

  @Override
  public Phone getNullableResult(final ResultSet rs, final int columnIndex)
      throws SQLException {
    final String str = rs.getString(columnIndex);
    if (str != null) {
      try {
        return codec.decode(str);
      } catch (final DecodingException e) {
        logger.error("An error occurs: {}", e.getMessage(), e);
        throw new SQLException(e);
      }
    }
    return null;
  }

  @Override
  public Phone getNullableResult(final CallableStatement cs, final int columnIndex)
      throws SQLException {
    final String str = cs.getString(columnIndex);
    if (str != null) {
      try {
        return codec.decode(str);
      } catch (final DecodingException e) {
        logger.error("An error occurs: {}", e.getMessage(), e);
        throw new SQLException(e);
      }
    }
    return null;
  }
}
