import java.util.*;

/**
 * 會員 (Member) 類別
 */
class Member {
    private int memberId;
    private String name;
    private String email;

    public Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{id=" + memberId + ", name='" + name + "', email='" + email + "'}";
    }
}

/**
 * 二元搜尋樹節點 (儲存 Member 物件)
 */
class MemberNode {
    Member member;
    MemberNode left;
    MemberNode right;

    public MemberNode(Member member) {
        this.member = member;
        this.left = null;
        this.right = null;
    }

    public int getKey() {
        return member.getMemberId();
    }
}

/**
 * 會員 BST 索引
 * 以 memberId 作為 key，完成 add、find、updateEmail、remove 與 inorder report
 */
public class MemberBstIndex {
    private MemberNode root;
    private int size;

    public MemberBstIndex() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增會員 (Id 不可重複)
     */
    public boolean addMember(int memberId, String name, String email) {
        // 檢查 email 是否為空白
        if (email == null || email.trim().isEmpty()) {
            System.out.println("錯誤: email 不得為空白");
            return false;
        }

        // 檢查 Id 是否已存在 (不可重複)
        if (findMember(memberId) != null) {
            System.out.println("錯誤: 會員 Id " + memberId + " 已存在，不可重複");
            return false;
        }

        Member newMember = new Member(memberId, name, email);
        root = addRecursive(root, newMember);
        size++;
        System.out.println("成功新增會員: " + newMember);
        return true;
    }

    private MemberNode addRecursive(MemberNode node, Member member) {
        if (node == null) {
            return new MemberNode(member);
        }

        int key = member.getMemberId();
        if (key < node.getKey()) {
            node.left = addRecursive(node.left, member);
        } else if (key > node.getKey()) {
            node.right = addRecursive(node.right, member);
        }
        // 等於的情況已在外部處理
        return node;
    }

    /**
     * 根據 memberId 尋找會員
     */
    public Member findMember(int memberId) {
        MemberNode result = findRecursive(root, memberId);
        return result != null ? result.member : null;
    }

    private MemberNode findRecursive(MemberNode node, int memberId) {
        if (node == null) {
            return null;
        }

        if (memberId == node.getKey()) {
            return node;
        }

        if (memberId < node.getKey()) {
            return findRecursive(node.left, memberId);
        } else {
            return findRecursive(node.right, memberId);
        }
    }

    /**
     * 更新會員 email (email 不得為 blank)
     */
    public boolean updateEmail(int memberId, String newEmail) {
        // 檢查 email 是否為空白
        if (newEmail == null || newEmail.trim().isEmpty()) {
            System.out.println("錯誤: email 不得為空白");
            return false;
        }

        MemberNode node = findRecursive(root, memberId);
        if (node == null) {
            System.out.println("錯誤: 找不到會員 Id " + memberId);
            return false;
        }

        String oldEmail = node.member.getEmail();
        node.member.setEmail(newEmail);
        System.out.println("成功更新 email: 會員 " + memberId + " 的 email 從 '" + oldEmail + "' 改為 '" + newEmail + "'");
        return true;
    }

    /**
     * 移除會員
     */
    public boolean removeMember(int memberId) {
        if (!contains(memberId)) {
            System.out.println("錯誤: 找不到會員 Id " + memberId);
            return false;
        }

        Member removedMember = findMember(memberId);
        root = removeRecursive(root, memberId);
        size--;
        System.out.println("成功移除會員: " + removedMember);
        return true;
    }

    private MemberNode removeRecursive(MemberNode node, int memberId) {
        if (node == null) {
            return null;
        }

        if (memberId < node.getKey()) {
            node.left = removeRecursive(node.left, memberId);
        } else if (memberId > node.getKey()) {
            node.right = removeRecursive(node.right, memberId);
        } else {
            // 找到要刪除的節點

            // Case 1: 葉節點
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: 只有一個子節點
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // Case 3: 有兩個子節點
            int successorId = findMinKey(node.right);
            MemberNode successorNode = findRecursive(node.right, successorId);
            node.member = successorNode.member;
            node.right = removeRecursive(node.right, successorId);
        }
        return node;
    }

    private int findMinKey(MemberNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.getKey();
    }

    /**
     * 檢查會員是否存在
     */
    public boolean contains(int memberId) {
        return findMember(memberId) != null;
    }

    /**
     * 取得樹的大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 中序走訪報告 (按 memberId 排序)
     */
    public List<Member> inorderReport() {
        List<Member> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(MemberNode node, List<Member> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.member);
            inorderRecursive(node.right, result);
        }
    }

    /**
     * 印出中序走訪報告 (格式化)
     */
    public void printInorderReport() {
        List<Member> members = inorderReport();
        System.out.println("===== 會員清單 (按 ID 排序) =====");
        System.out.println("總會員數: " + size);
        if (members.isEmpty()) {
            System.out.println("(尚無會員)");
        } else {
            for (Member m : members) {
                System.out.println("  " + m);
            }
        }
        System.out.println("===================================");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     會員 BST 索引系統");
        System.out.println("========================================\n");

        MemberBstIndex memberIndex = new MemberBstIndex();

        // ========== 測試新增會員 ==========
        System.out.println("【測試一】新增會員");
        System.out.println("----------------------------------------");

        // 正常新增
        memberIndex.addMember(1001, "王小明", "xiaoming.wang@example.com");
        memberIndex.addMember(1003, "陳小華", "xiaohua.chen@example.com");
        memberIndex.addMember(1005, "林小美", "xiaomei.lin@example.com");
        memberIndex.addMember(1002, "張小強", "xiaoqiang.zhang@example.com");
        memberIndex.addMember(1004, "李小英", "xiaoying.li@example.com");
        System.out.println();

        memberIndex.printInorderReport();
        System.out.println();

        // ========== 測試重複 Id ==========
        System.out.println("【測試二】測試重複 Id (不可重複)");
        System.out.println("----------------------------------------");
        memberIndex.addMember(1001, "重複測試", "duplicate@example.com");
        System.out.println();

        // ========== 測試空白 email ==========
        System.out.println("【測試三】測試空白 email (不得為 blank)");
        System.out.println("----------------------------------------");
        memberIndex.addMember(1006, "測試空白", "");
        memberIndex.addMember(1007, "測試空白", "   ");
        memberIndex.addMember(1008, "測試空白", null);
        System.out.println();

        // ========== 測試尋找會員 ==========
        System.out.println("【測試四】尋找會員");
        System.out.println("----------------------------------------");
        int searchId = 1003;
        Member found = memberIndex.findMember(searchId);
        System.out.println("尋找 ID " + searchId + ": " + (found != null ? found : "找不到"));
        
        searchId = 1099;
        found = memberIndex.findMember(searchId);
        System.out.println("尋找 ID " + searchId + ": " + (found != null ? found : "找不到"));
        System.out.println();

        // ========== 測試更新 email ==========
        System.out.println("【測試五】更新 Email");
        System.out.println("----------------------------------------");
        memberIndex.updateEmail(1002, "new.email@example.com");
        memberIndex.updateEmail(1004, "updated@example.com");
        memberIndex.updateEmail(1001, "xiaoming.new@example.com");
        System.out.println();

        // 測試更新不存在的會員
        memberIndex.updateEmail(9999, "nonexist@example.com");
        System.out.println();

        // 測試更新為空白 email
        memberIndex.updateEmail(1002, "");
        memberIndex.updateEmail(1002, "   ");
        memberIndex.updateEmail(1002, null);
        System.out.println();

        memberIndex.printInorderReport();
        System.out.println();

        // ========== 測試移除會員 ==========
        System.out.println("【測試六】移除會員");
        System.out.println("----------------------------------------");

        // 移除葉節點 (例如 1001)
        memberIndex.removeMember(1001);
        System.out.println();

        // 移除只有一個子節點的節點 (例如 1003)
        memberIndex.removeMember(1003);
        System.out.println();

        // 移除有兩個子節點的節點 (例如 1002)
        memberIndex.removeMember(1002);
        System.out.println();

        // 測試移除不存在的會員
        memberIndex.removeMember(9999);
        System.out.println();

        memberIndex.printInorderReport();
        System.out.println();

        // ========== 新增更多會員測試 ==========
        System.out.println("【測試七】新增更多會員");
        System.out.println("----------------------------------------");
        memberIndex.addMember(2001, "吳大偉", "dawei.wu@example.com");
        memberIndex.addMember(2003, "鄭小芳", "xiaofang.zheng@example.com");
        memberIndex.addMember(2002, "蔡小婷", "xiaoting.cai@example.com");
        System.out.println();

        memberIndex.printInorderReport();
        System.out.println();

        // ========== 最終測試 ==========
        System.out.println("【測試八】綜合操作測試");
        System.out.println("----------------------------------------");
        System.out.println("當前會員數: " + memberIndex.getSize());
        System.out.println("嘗試新增已存在的會員: 1004");
        memberIndex.addMember(1004, "重複測試", "duplicate@example.com");
        System.out.println("更新會員 2001 的 email");
        memberIndex.updateEmail(2001, "dawei.new@example.com");
        System.out.println("移除會員 2003");
        memberIndex.removeMember(2003);
        System.out.println();

        memberIndex.printInorderReport();
        System.out.println();

        System.out.println("========================================");
        System.out.println("         會員索引系統執行完畢！");
        System.out.println("========================================");

        // 顯示功能總結
        System.out.println("\n【功能總結】");
        System.out.println("1. 新增會員 (addMember): 使用 memberId 作為 key，email 不得為空白，Id 不可重複");
        System.out.println("2. 尋找會員 (findMember): 根據 memberId 快速尋找");
        System.out.println("3. 更新 Email (updateEmail): 更新指定會員的 email，email 不得為空白");
        System.out.println("4. 移除會員 (removeMember): 根據 memberId 移除會員 (支援三種刪除情況)");
        System.out.println("5. 中序報告 (inorderReport): 按 memberId 排序輸出所有會員");
        System.out.println("6. 會員總數 (getSize): 取得目前會員人數");
    }
}