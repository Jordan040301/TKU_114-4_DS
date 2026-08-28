/**
 * 檔名：Q07_RequestPipeline.java
 * 功能：Stack 與 Queue 請求流程
 * 說明：isBalanced() 使用 Deque 檢查括號配對
 *       process() 使用兩個 Queue 處理一般/緊急請求
 */

import java.util.*;

public class Q07_RequestPipeline {

    // ========== Part 1: 括號平衡檢查 ==========

    /**
     * 檢查文字中的括號是否正確配對和巢狀
     * @param text 要檢查的文字
     * @return true 表示括號正確配對，false 表示不正確或 text 為 null
     */
    public static boolean isBalanced(String text) {
        // null 回傳 false
        if (text == null) {
            return false;
        }

        // 空字符串回傳 true
        if (text.isEmpty()) {
            return true;
        }

        // 使用 Deque<Character> 作為 Stack
        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : text.toCharArray()) {
            // 如果是左括號，推入 stack
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            }
            // 如果是右括號，檢查配對
            else if (ch == ')' || ch == ']' || ch == '}') {
                // 如果 stack 為空，表示沒有對應的左括號
                if (stack.isEmpty()) {
                    return false;
                }

                char top = stack.pop();
                // 檢查是否配對
                if (!isMatchingPair(top, ch)) {
                    return false;
                }
            }
            // 其他字元忽略
        }

        // 如果 stack 為空，表示所有括號都正確配對
        return stack.isEmpty();
    }

    /**
     * 檢查兩個括號是否配對
     * @param open 左括號
     * @param close 右括號
     * @return true 表示配對
     */
    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '[' && close == ']') ||
               (open == '{' && close == '}');
    }

    // ========== Part 2: 請求處理管道 ==========

    /**
     * 處理請求命令
     * @param commands 命令陣列
     * @return 處理結果列表
     */
    public static List<String> process(String[] commands) {
        // commands 為 null 時回傳 empty List
        if (commands == null) {
            return Collections.emptyList();
        }

        // 使用 Deque<String> 作為兩個 Queue
        // 一般請求佇列
        Deque<String> normalQueue = new ArrayDeque<>();
        // 緊急請求佇列（優先處理）
        Deque<String> urgentQueue = new ArrayDeque<>();

        List<String> results = new ArrayList<>();

        for (String command : commands) {
            // 空白 command 或 null command 必須忽略
            if (command == null || command.trim().isEmpty()) {
                continue;
            }

            // 解析命令
            String trimmed = command.trim();
            String[] parts = trimmed.split(" ", 2);  // 最多分割成 2 部分

            // 格式錯誤（沒有空格或只有命令沒有參數）必須忽略
            if (parts.length < 2) {
                continue;
            }

            String cmdType = parts[0].toUpperCase();
            String id = parts[1].trim();

            // 如果 id 為空，忽略
            if (id.isEmpty()) {
                continue;
            }

            switch (cmdType) {
                case "NORMAL":
                    // 加入一般 Queue 尾端
                    normalQueue.offer(id);
                    break;

                case "URGENT":
                    // 加入緊急 Queue 尾端
                    urgentQueue.offer(id);
                    break;

                case "PROCESS":
                    // 優先處理最早加入的緊急請求
                    if (!urgentQueue.isEmpty()) {
                        results.add(urgentQueue.poll());
                    } else if (!normalQueue.isEmpty()) {
                        // 若無緊急請求，處理一般請求
                        results.add(normalQueue.poll());
                    } else {
                        // 兩者皆空時加入 "EMPTY" 至結果
                        results.add("EMPTY");
                    }
                    break;

                default:
                    // 未知命令類型必須忽略
                    break;
            }
        }

        // 回傳不可修改的結果列表
        return Collections.unmodifiableList(results);
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        System.out.println(Q07_RequestPipeline.isBalanced("a[b[c](d)]"));  // true
        System.out.println(Q07_RequestPipeline.isBalanced("[[1]]"));       // false
        System.out.println();

        String[] commands1 = {
            "NORMAL N",
            "URGENT U",
            "NORMAL N2",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println(Q07_RequestPipeline.process(commands1));
        System.out.println();

        // ===== isBalanced 測試 =====
        System.out.println("===== isBalanced 測試 =====");
        
        // 測試 null 和空字串
        System.out.println("isBalanced(null) → " + isBalanced(null));          // false
        System.out.println("isBalanced(\"\") → " + isBalanced(""));            // true
        System.out.println();

        // 測試正確配對
        System.out.println("isBalanced(\"()\") → " + isBalanced("()"));        // true
        System.out.println("isBalanced(\"[]\") → " + isBalanced("[]"));        // true
        System.out.println("isBalanced(\"{}\") → " + isBalanced("{}"));        // true
        System.out.println("isBalanced(\"([{}])\") → " + isBalanced("([{}])")); // true
        System.out.println("isBalanced(\"a(b[c]d)e\") → " + isBalanced("a(b[c]d)e")); // true
        
        // 測試不正確配對
        System.out.println("isBalanced(\"(\") → " + isBalanced("("));           // false
        System.out.println("isBalanced(\")\") → " + isBalanced(")"));           // false
        System.out.println("isBalanced(\"([)]\") → " + isBalanced("([)]"));     // false
        System.out.println("isBalanced(\"({)}[)\") → " + isBalanced("({)}[)")); // false
        System.out.println("isBalanced(\"((()))\") → " + isBalanced("((()))")); // true
        System.out.println();

        // ===== process 測試 =====
        System.out.println("===== process 測試 =====");

        // 測試基本功能
        String[] commands2 = {
            "NORMAL A",
            "URGENT B",
            "NORMAL C",
            "PROCESS",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("基本功能測試: " + process(commands2));
        // 預期: [B, A, C, EMPTY]

        // 測試緊急優先
        String[] commands3 = {
            "NORMAL A",
            "URGENT B",
            "URGENT C",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("緊急優先測試: " + process(commands3));
        // 預期: [B, C, A]

        // 測試只有一般請求
        String[] commands4 = {
            "NORMAL X",
            "NORMAL Y",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("只有一般請求: " + process(commands4));
        // 預期: [X, Y, EMPTY]

        // 測試只有緊急請求
        String[] commands5 = {
            "URGENT P",
            "URGENT Q",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("只有緊急請求: " + process(commands5));
        // 預期: [P, Q, EMPTY]

        // 測試混合請求
        String[] commands6 = {
            "NORMAL A",
            "NORMAL B",
            "URGENT C",
            "PROCESS",
            "NORMAL D",
            "URGENT E",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("混合請求測試: " + process(commands6));
        // 預期: [C, E, A, B, D]
        System.out.println();

        // ===== 無效命令處理測試 =====
        System.out.println("===== 無效命令處理測試 =====");

        // 測試 null commands
        System.out.println("process(null) → " + process(null));  // []

        // 測試空白命令
        String[] commands7 = {
            "NORMAL A",
            "",
            "   ",
            "URGENT B",
            null,
            "PROCESS"
        };
        System.out.println("空白/null 命令測試: " + process(commands7));
        // 預期: [B]

        // 測試格式錯誤
        String[] commands8 = {
            "NORMAL",
            "URGENT",
            "PROCESS",
            "NORMAL A",
            "PROCESS"
        };
        System.out.println("格式錯誤測試: " + process(commands8));
        // 預期: [EMPTY, A]

        // 測試未知命令類型
        String[] commands9 = {
            "NORMAL A",
            "UNKNOWN X",
            "URGENT B",
            "PROCESS"
        };
        System.out.println("未知命令類型測試: " + process(commands9));
        // 預期: [B]

        // 測試 id 為空白
        String[] commands10 = {
            "NORMAL ",
            "URGENT ",
            "NORMAL A",
            "PROCESS"
        };
        System.out.println("id 空白測試: " + process(commands10));
        // 預期: [A]
        System.out.println();

        // ===== 綜合測試 =====
        System.out.println("===== 綜合測試 =====");
        String[] commands11 = {
            "NORMAL ID1",
            "URGENT ID2",
            "NORMAL ID3",
            "PROCESS",
            "URGENT ID4",
            "NORMAL ID5",
            "PROCESS",
            "PROCESS",
            "PROCESS",
            "PROCESS"
        };
        System.out.println("綜合測試: " + process(commands11));
        // 預期: [ID2, ID4, ID1, ID3, ID5, EMPTY]
    }
}