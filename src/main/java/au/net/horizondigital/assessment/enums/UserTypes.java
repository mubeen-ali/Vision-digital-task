package au.net.horizondigital.assessment.enums;

import lombok.Getter;

@Getter
public enum UserTypes {
    CUSTOMER("customer"), SHOP_OWNER("owner"), SHOP_OPERATOR("operator");

    private String value;

    UserTypes(String value) {
        this.value = value;
    }

}
