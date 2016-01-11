package com.xetlab.jxlexcel;

import com.xetlab.jxlexcel.conf.validator.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by gordian on 2016/1/6.
 */
public class ValidatorTest extends Assert {

    @Test
    public void testMobile() {
        Validator validator = new MobileValidator();
        assertFalse(validator.validate("123414"));
        assertFalse(validator.validate("asdfasfd"));
        assertFalse(validator.validate("28888888888"));
        assertFalse(validator.validate("12345768987"));
        assertTrue(validator.validate("13945768987"));
        assertTrue(validator.validate("13345768987"));
        assertFalse(validator.validate("19945768987"));
    }

    @Test
    public void testEmail() {
        Validator validator = new EmailValidator();
        assertFalse(validator.validate("123414"));
        assertFalse(validator.validate("asdfasfd"));
        assertFalse(validator.validate("28888888888"));
        assertFalse(validator.validate("12345768987"));
        assertTrue(validator.validate("13945768987@qq.com"));
        assertTrue(validator.validate("13345768987@163.com"));
        assertFalse(validator.validate("19945768987@@..com"));
    }

    @Test
    public void testChinese() {
        Validator validator = new ChineseValidator();
        assertFalse(validator.validate("123414"));
        assertTrue(validator.validate("中文"));
    }

    @Test
    public void testEnglish() {
        Validator validator = new EnglishValidator();
        assertFalse(validator.validate("中文"));
        assertTrue(validator.validate("abd"));
    }

    @Test
    public void testNum() {
        Validator validator = new NumValidator();
        assertFalse(validator.validate("中文"));
        assertTrue(validator.validate("-22"));
        assertTrue(validator.validate("22"));
        assertTrue(validator.validate("22.22"));
    }

    @Test
    public void testMin() {
        MinValidator validator = new MinValidator();
        validator.setMin(2f);
        assertFalse(validator.validate("中文"));
        assertFalse(validator.validate("-22"));
        assertFalse(validator.validate("0.1"));
        assertTrue(validator.validate("22.22"));
    }

    @Test
    public void testMax() {
        MaxValidator validator = new MaxValidator();
        validator.setMax(10.5f);
        assertFalse(validator.validate("中文"));
        assertTrue(validator.validate("-22"));
        assertTrue(validator.validate("0.1"));
        assertFalse(validator.validate("22.22"));
    }

    @Test
    public void testRange() {
        RangeValidator validator = new RangeValidator();
        validator.setMin(3f);
        validator.setMax(10.5f);
        assertFalse(validator.validate("中文"));
        assertFalse(validator.validate("-22"));
        assertFalse(validator.validate("0.1"));
        assertTrue(validator.validate("4"));
    }

    @Test
    public void testMinLength() {
        MinLengthValidator validator = new MinLengthValidator();
        validator.setMinLength(2);
        assertTrue(validator.validate("中文"));
        assertTrue(validator.validate("-22"));
        assertTrue(validator.validate("0.1"));
        assertTrue(validator.validate("22.22"));
        assertFalse(validator.validate("2"));
    }

    @Test
    public void testMaxLength() {
        MaxLengthValidator validator = new MaxLengthValidator();
        validator.setMaxLength(10);
        assertTrue(validator.validate("中文"));
        assertTrue(validator.validate("-22"));
        assertFalse(validator.validate("0.1asfdasdfasfas"));
        assertTrue(validator.validate("22.22"));
    }

    @Test
    public void testRangeLength() {
        RangeLengthValidator validator = new RangeLengthValidator();
        validator.setMinLength(3);
        validator.setMaxLength(10);
        assertFalse(validator.validate("中文"));
        assertTrue(validator.validate("-22"));
        assertTrue(validator.validate("0.1"));
        assertFalse(validator.validate("4"));
    }

    @Test
    public void testRequired() {
        Validator validator = new RequiredValidator();
        assertFalse(validator.validate(""));
        assertTrue(validator.validate("4"));
    }
}
