import java.util.Objects;

/**
 * 泛型結果封裝類別
 * @param <T> 資料型態
 */
class Result<T> {
    private final boolean success;
    private final String message;
    private final T data;

    // 私有建構子，透過靜態工廠方法建立
    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 建立成功結果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", data);
    }

    /**
     * 建立成功結果（帶自訂訊息）
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, message, data);
    }

    /**
     * 建立失敗結果
     */
    public static <T> Result<T> failure(String message) {
        return new Result<>(false, message, null);
    }

    // ---------- Getter ----------
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    /**
     * 安全取得資料，失敗時拋出例外
     */
    public T getDataOrThrow() {
        if (!success) {
            throw new IllegalStateException("操作失敗: " + message);
        }
        return data;
    }

    /**
     * 取得資料，失敗時回傳預設值
     */
    public T getDataOrDefault(T defaultValue) {
        return success ? data : defaultValue;
    }

    @Override
    public String toString() {
        return String.format("Result{success=%s, message='%s', data=%s}",
                success, message, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result<?> result = (Result<?>) o;
        return success == result.success &&
                Objects.equals(message, result.message) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, data);
    }
}

/**
 * 測試類別
 */
public class GenericResultDemo {
    public static void main(String[] args) {
        System.out.println("========== 測試 Result<String> ==========");
        testStringResult();

        System.out.println("\n========== 測試 Result<Integer> ==========");
        testIntegerResult();

        System.out.println("\n========== 測試失敗場景 ==========");
        testFailureScenario();

        System.out.println("\n========== 編譯時期型態安全驗證 ==========");
        testCompileTimeSafety();
    }

    /**
     * 測試字串型態結果
     */
    private static void testStringResult() {
        // 模擬從資料庫取得使用者名稱
        Result<String> nameResult = fetchUserName("S001");

        if (nameResult.isSuccess()) {
            // 編譯時期確保 data 是 String 型態
            String name = nameResult.getData();
            System.out.println("取得使用者名稱成功: " + name);
            System.out.println("名稱長度: " + name.length());
        } else {
            System.out.println("取得失敗: " + nameResult.getMessage());
        }
    }

    /**
     * 模擬取得使用者名稱的業務方法
     */
    private static Result<String> fetchUserName(String studentId) {
        if ("S001".equals(studentId)) {
            return Result.success("王小明");
        } else if ("S002".equals(studentId)) {
            return Result.success("李小華");
        } else {
            return Result.failure("未找到學號: " + studentId + " 對應的學生");
        }
    }

    /**
     * 測試整數型態結果
     */
    private static void testIntegerResult() {
        // 模擬計算總分
        Result<Integer> scoreResult = calculateTotalScore("S001");

        if (scoreResult.isSuccess()) {
            // 編譯時期確保 data 是 Integer 型態
            int score = scoreResult.getData();
            System.out.println("總分為: " + score);
            System.out.println("是否及格: " + (score >= 60 ? "是" : "否"));
        } else {
            System.out.println("計算失敗: " + scoreResult.getMessage());
        }
    }

    /**
     * 模擬計算總分的業務方法
     */
    private static Result<Integer> calculateTotalScore(String studentId) {
        if ("S001".equals(studentId)) {
            return Result.success(85);
        } else if ("S002".equals(studentId)) {
            return Result.success(92);
        } else {
            return Result.failure("未找到學號: " + studentId + " 的成績資料");
        }
    }

    /**
     * 測試失敗場景 - 驗證 data == null
     */
    private static void testFailureScenario() {
        Result<String> failResult = Result.failure("資料庫連線逾時");

        System.out.println("成功狀態: " + failResult.isSuccess());
        System.out.println("錯誤訊息: " + failResult.getMessage());
        System.out.println("資料是否為 null: " + (failResult.getData() == null));

        // 使用 getDataOrDefault 提供預設值
        String safeValue = failResult.getDataOrDefault("預設值");
        System.out.println("安全取值（帶預設值）: " + safeValue);

        // 示範 getDataOrThrow（會拋出例外）
        try {
            failResult.getDataOrThrow();
        } catch (IllegalStateException e) {
            System.out.println("拋出例外: " + e.getMessage());
        }
    }

    /**
     * 驗證編譯時期型態安全
     * 這裡展示如果型態不匹配，編譯階段就會報錯
     */
    private static void testCompileTimeSafety() {
        // ✅ 正確：Result<String> 只能接收 String
        Result<String> stringResult = Result.success("這是字串");
        String str = stringResult.getData();  // 編譯通過

        // ✅ 正確：Result<Integer> 只能接收 Integer
        Result<Integer> intResult = Result.success(100);
        Integer num = intResult.getData();    // 編譯通過

        // ❌ 下面這行如果取消註解，編譯會報錯（型態不匹配）
        // Result<String> errorResult = Result.success(123);  // 編譯錯誤！

        System.out.println("✅ 型態安全驗證通過！");
        System.out.println("stringResult 資料: " + stringResult.getData());
        System.out.println("intResult 資料: " + intResult.getData());

        // 示範不同型態的泛型在執行時期不會混淆
        System.out.println("stringResult 型態: " + stringResult.getClass().getName());
        System.out.println("intResult 型態: " + intResult.getClass().getName());
    }
}