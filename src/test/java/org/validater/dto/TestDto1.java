package org.validater.dto;

import org.validater.annotations.Max;

public class TestDto1 {
    @Max(20)
    int field1 = 30;
}
