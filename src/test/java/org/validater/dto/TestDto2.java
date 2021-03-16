package org.validater.dto;

import org.validater.annotations.Max;

public class TestDto2 {
    @Max(30)
    int field1 = 40;
}
