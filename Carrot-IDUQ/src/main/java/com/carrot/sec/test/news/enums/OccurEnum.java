package com.carrot.sec.test.news.enums;

public class OccurEnum {

    public static enum Occur {

        /**
         * 必须是
         */
        MUST {
            @Override
            public String toString() {
                return "+";
            }
        },

        /**
         * 排除掉
         */
        FILTER {
            @Override
            public String toString() {
                return "#";
            }
        },

        /**
         * 应该是
         */
        SHOULD {
            @Override
            public String toString() {
                return "";
            }
        },

        /**
         * 一定不是
         */
        MUST_NOT {
            @Override
            public String toString() {
                return "-";
            }
        };

    }

    ;

    private OccurEnum occurEnum;

    @Override
    public String toString() {
        return occurEnum.toString() + occurEnum.toString();
    }

}
