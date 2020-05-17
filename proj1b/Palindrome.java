public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> container = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            container.addLast(word.charAt(i));
        }
        return container;
    }

    public boolean isPalindrome(String word) {
        if (word.length() == 1 || word.length() == 0) {
            return true;
        }
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque);

    }

    private boolean isPalindrome(Deque<Character> deque) {
        if (deque.size() == 1 || deque.size() == 0) {
            return true;
        } else if (deque.removeFirst() != deque.removeLast()) {
            return false;
        }
        return isPalindrome(deque);
    }

    /**
     * Judge whether a String is palindrome by a third method.
     * @param word
     * @param cc
     * @return
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        String text = "foo";
//        char charAtZero = text.charAt(2);
//        System.out.println(charAtZero); // Prints f
//        System.out.println(text.length());
//        Palindrome palindrome = new Palindrome();
//        System.out.println(palindrome.isPalindrome("asdsa"));
//
//    }
}
