
package highscore;

import java.util.EnumSet;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import models.JeopardyUser.Gender;


/**
 * <p>Java-Klasse für GenderType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="GenderType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="female"/>
 *     &lt;enumeration value="male"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GenderType")
@XmlEnum
public enum GenderType {

    @XmlEnumValue("female")
    FEMALE("female", Gender.female),
    @XmlEnumValue("male")
    MALE("male", Gender.male);
    private final String value;
    private final Gender jeopardyGender;

    GenderType(String v, Gender jeopardyGender) {
        value = v;
        this.jeopardyGender = jeopardyGender;
    }

    public String value() {
        return value;
    }

    public static GenderType fromValue(String v) {
        for (GenderType c: GenderType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static GenderType getByJeopardyGender(Gender jeopardyGender)
    {
    	for(GenderType g : EnumSet.allOf(GenderType.class))
    	{
    		if(g.jeopardyGender == jeopardyGender)
    			return g;
    	}
    	return null;
    }
}
