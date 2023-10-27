package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class Organization {
  private Long id;
  private String name;
  private String address;
}
