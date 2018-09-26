package org.gorgeous.fitness.util

import groovy.transform.CompileStatic

import java.security.SecureRandom

/**
 * 随机码生成器 .
 *
 * @project nkp
 * @author L.Yang create on 2018/8/29 下午4:39
 */
@CompileStatic
class RandomCode {
    /** 大写字母池. */
    private static final List<String> UPPER_CASE_POOL =
            ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
    /** 小写字母池. */
    private static final List<String> LOWER_CASE_POOL =
            ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]
    /** 数字池. */
    private static final List<String> NUMBER_POOL = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]

    /** 长度. */
    private int length

    /** 数量. */
    private int size

    /** 随机码类型集合. */
    private Set<Type> types = []

    private List<String> excludeChars = []

    /**
     * 构造方法.
     *
     * @param type
     *              随机码类型.
     * @param length
     *              单个随机码长度.
     * @param size
     *              随机码数量.
     */
    private RandomCode(int length, int size) {
        this.length = length
        this.size = size
    }

    /**
     * 随机码 随机实例
     *
     * @param length 随机码长度
     * @param size 随机码个数
     * @return 随机实例
     */
    static RandomCode newInstance(int length, int size) {
        return new RandomCode(length, size)
    }

    /**
     * 使用大写字母.
     *
     * @param required 是否必须包含（默认false）
     */
    RandomCode useUppercase(boolean required = false) {
        Type.UPPERCASE.required = required
        types.add(Type.UPPERCASE)
        return this
    }

    /**
     * 使用小写字母.
     *
     * @param required 是否必须包含（默认false）
     */
    RandomCode useLowercase(boolean required = false) {
        Type.LOWERCASE.required = required
        types.add(Type.LOWERCASE)
        return this
    }

    /**
     * 使用数字.
     *
     * @param required 是否必须包含（默认false）
     */
    RandomCode useNumeric(boolean required = false) {
        Type.NUMBER.required = required
        types.add(Type.NUMBER)
        return this
    }

    /**
     * 忽略字符.
     *
     * @param excludeChar 要忽略的字符
     */
    RandomCode exclude(String... excludeChar) {
        excludeChars.addAll(excludeChar.toList())
        return this
    }

    /**
     * 生成随机码
     * @return 随机码列表
     */
    List<String> random() {
        check(size, length, types)
        List<String> result = []
        size.times {
            result << generateCode(length, types, excludeChars)
        }
        return result
    }

    /**
     * 校验 生成参数
     * <pre>
     *     size 必须大于0
     *     length 必须大于等于 必选类型的个数
     * </pre>
     * @param size 随机码个数
     * @param length 单个随机码长度
     * @param types 随机码类型
     */
    private static void check(int size, int length, Set<Type> types) {
        if (size <= 0) {
            throw new RuntimeException('size must greater then 0')
        }

        if (!types) {
            throw new RuntimeException('one type of code should set: uppercase/lowercase/numeric')
        }

        int requiredTypes = types.count { it.required }.intValue()

        if (requiredTypes > length) {
            throw new RuntimeException("the length: ${length} must greater then required type's size: ${requiredTypes} ")
        }
    }

    /**
     * 生成随机码
     *
     * @param length 单个随机码长度
     * @param types 随机码类型
     * @param excludeChars 要忽略的字符
     * @return 随机码
     */
    private static String generateCode(int length, Set<Type> types, List<String> excludeChars) {
        List<String> pool = getCodePool(types)
        Random random = new SecureRandom()
        String code = ''
        while (needGenerate(code, types, excludeChars)) {
            code = generate(length, pool, random)
        }
        return code
    }

    /**
     * 是否需要重试
     *
     * <pre>
     *     如果随机码为空，则需要进行generate
     *     如果随机码不为空，则需要判断 随机码 是否满足 随机码类型中的必选要求
     *     如果随机码包含忽略字符串，则需要进行生成
     * </pre>
     *
     * @param code 随机码
     * @param types 随机码类型
     * @param excludeChars 要忽略的字符
     *
     * @return 是否需要进行生成
     */
    private static boolean needGenerate(String code, Set<Type> types, List<String> excludeChars) {
        if (!code) {
            return true
        }

        return checkRequired(code, types) ?: checkExclude(code, excludeChars)
    }


    private static boolean checkRequired(String code, Set<Type> types) {
        boolean retry = false
        for (Type type : types.findResults { if (it.required) it }) {
            boolean contains = false
            for (int i = 0; i < code.length(); i++) {
                if (type.pool.contains(code.charAt(i).toString())) {
                    contains = true
                    break
                }
            }

            if (!contains) {
                retry = true
                break
            }
        }
        return retry
    }

    private static checkExclude(String code, List<String> excludeChars) {
        boolean retry = false
        for (int i = 0; i < code.length(); i++) {
            if (excludeChars.contains(code.charAt(i).toString())) {
                retry = true
                break
            }
        }
        return retry
    }

    /**
     * 生成随机码
     *
     * @param length 随机码长度
     * @param pool 随机码字符池
     * @param random 随机实例
     * @return 随机码
     */
    private static String generate(int length, List<String> pool, Random random) {
        String code = ""
        int index
        int counts = 0
        while (counts < length) {
            index = random.nextInt(pool.size() - 1)
            code += pool[index]
            counts++
        }
        return code
    }

    /** 获取数据池. */
    private static List<String> getCodePool(Set<Type> types) {
        List<String> dataPool = []
        types.each { dataPool.addAll(it.pool) }
        return dataPool
    }

    /** 随机码类型 枚举. */
    private static enum Type {

        /** 数字. */
        NUMBER(NUMBER_POOL),

        /** 大写字母. */
        UPPERCASE(UPPER_CASE_POOL),

        /** 小写字母. */
        LOWERCASE(LOWER_CASE_POOL)

        /** 是否是必须包含 */
        boolean required

        List<String> pool

        Type(List<String> pool) {
            this.pool = pool
        }

    }

}
