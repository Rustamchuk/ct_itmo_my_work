public class SumFloat {
    public static void main(String[] args) {
        float amount = 0.0f;
        StringBuilder inputStr = new StringBuilder();
        
        for (int i = 0; i < args.length; i++) {
		    inputStr.append(args[i] + " ");
		}

        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < inputStr.length(); i++) {
            if (!Character.isWhitespace(inputStr.charAt(i))) {
                currentNumber.append(inputStr.charAt(i));
            } else if (currentNumber.length() > 0) {
			    amount += Float.parseFloat(currentNumber.toString());
                currentNumber.setLength(0);
		    }
	    }

        System.out.println(amount);
    }
}