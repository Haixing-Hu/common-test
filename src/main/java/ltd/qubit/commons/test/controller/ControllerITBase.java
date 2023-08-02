////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.controller;

import ltd.qubit.commons.random.RandomBeanGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * 对 Controller 进行集成测试的类的基类。
 *
 * @author 胡海星
 */
public class ControllerITBase {

  @Autowired
  protected WebApplicationContext wac;

  protected MockMvc mockMvc;

  protected ObjectMapper objectMapper;

  protected ObjectWriter objectWriter;

  protected RandomBeanGenerator random = new RandomBeanGenerator();

  @Autowired
  public final void setMessageConverter(
      final MappingJackson2HttpMessageConverter messageConverter) {
    objectMapper = messageConverter.getObjectMapper();
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
  }

  @BeforeEach
  public void setup() {
    final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeHeaders(true);
    filter.setIncludeClientInfo(true);
    filter.setIncludePayload(true);
    filter.setIncludeQueryString(true);
    filter.setIncludeQueryString(true);
    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                             .addFilter(filter)
                             .build();
  }

}
