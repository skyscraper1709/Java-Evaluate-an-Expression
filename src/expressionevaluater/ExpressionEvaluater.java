 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressionevaluater;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Safat
 */
public class ExpressionEvaluater {

    String fullText = "";
    Stack<Character> stack = new Stack<Character>();
    Stack<Integer> values = new Stack<Integer>();
    HashMap hm = new HashMap();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ExpressionEvaluater ee = new ExpressionEvaluater();
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("E:\\\\NetBeans Project workspace\\\\ExpressionEvaluater\\\\src\\\\expressionevaluater\\input.txt"));

            if ((sCurrentLine = br.readLine()) != null) {
                int vars = Integer.parseInt(sCurrentLine.trim());
                System.out.println("Preparing to read " + vars + " variables");
                for (int i = 0; i < vars; i++) {
                    if ((sCurrentLine = br.readLine()) != null) {
                        String varLine = sCurrentLine.trim();
                        String[] tokenArray = varLine.split("=");
                        ee.hm.put(tokenArray[0].trim(), tokenArray[1].trim());
                    }
                }
//                System.out.println("current Hash Map Size is: " + ee.hm.size());
                Iterator it = ee.hm.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
//                    System.out.println("\nMap Contents:\n" + pair.getKey() + " = " + pair.getValue());
//                    it.remove(); // avoids a ConcurrentModificationException
                }
//                System.out.println("current Hash Map Size is: " + ee.hm.size());
            } else {
                System.out.println("No valid first line");
            }

            //
            String postFix = "";
            if ((sCurrentLine = br.readLine()) != null) {
                int exps = Integer.parseInt(sCurrentLine.trim());
                System.out.println("Preparing to read " + exps + " expressions");

                for (int i = 0; i < exps; i++) {
                    ee.stack=new Stack<Character>();
                    postFix = "";
                    System.out.println("\nReading expression no.  : " + i);
                    if ((sCurrentLine = br.readLine()) != null) {

                        String expLine = sCurrentLine.trim();

                        for (int j = 0; j < expLine.length(); j++) {
                            if (expLine.charAt(j) >= 'a' && expLine.charAt(j) <= 'z'
                                    || expLine.charAt(j) >= 'A' && expLine.charAt(j) <= 'Z') {
                                //add to string
//                                System.out.println("current: " + expLine.charAt(j));
//                                System.out.println("adding");
                                postFix += expLine.charAt(j);
                            } else if (expLine.charAt(j) == '+'
                                    || expLine.charAt(j) == '-'
                                    || expLine.charAt(j) == '*'
                                    || expLine.charAt(j) == '/' /*and stack is empty*/) {
//                                System.out.println("operator");

                                if (ee.stack.isEmpty()) {
//                                    System.out.println("is empty");
                                    ee.stack.push(expLine.charAt(j));
                                } else {
                                    if (!ee.stack.isEmpty()) {
//                                        System.out.println("is NOT empty");
//                                        System.out.println(expLine.charAt(j) + " , " + ee.stack.peek());
                                        if (ee.greater(expLine.charAt(j), ee.stack.peek())) {           
//                                            System.out.println("~greater~");
//                                            System.out.println("Comparing: "+expLine.charAt(j) +" & "+ ee.stack.peek());
//                                            System.out.println("pushing in while: " + expLine.charAt(j));
                                            ee.stack.push(expLine.charAt(j));
                                        } else  {
                                            while ( !ee.stack.isEmpty() && ee.less(expLine.charAt(j), ee.stack.peek()) ){
//                                            System.out.println("~smaller~");
//                                            System.out.println("Comparing: "+expLine.charAt(j) +" & "+ee.stack.peek());
//                                            System.out.println("adding to PostFIX: " +ee.stack.peek() );
                                            postFix += ee.stack.pop();
                                           
                                            }
                                            ee.stack.push(expLine.charAt(j));
                                        }

                                    }
                                    
                                }
                            }

                        }
                            while(!ee.stack.isEmpty()) postFix+=ee.stack.pop();
                    }
                    System.out.println("\n ------------ \nFINAL postFix: " + postFix+"\n ------------ \n");
                    
                    // Solve here
                    String temp="";
                    boolean compileError=false;
                    for (int j = 0; j < postFix.length(); j++) {
                        if(postFix.charAt(j)<='z' && postFix.charAt(j)>='a'
                                ||
                                postFix.charAt(j)<='Z' && postFix.charAt(j)>='A' ){
//                            System.out.println("that is: "+postFix.charAt(j)+" = "+Integer.parseInt((String)(ee.hm.get(""+postFix.charAt(j)))));
                           try{ temp+=Integer.parseInt((String)(ee.hm.get(""+postFix.charAt(j))));}
                           catch(Exception error){
                               System.out.println("Compilation Error: \n "+postFix.charAt(j)+" not defined in Expression "+i);
                               compileError=true;
                               break;
                           }
                            
                        }else{
                            temp+=postFix.charAt((j));
                        }
                    }
                    if(!compileError){
                    postFix=temp;
                    System.out.println("So string is now: "+postFix);
                    
                    ee.values=new Stack<Integer>();
                    
                    for (int k = 0; k < postFix.length(); k++) {    
                        
                        if(postFix.charAt(k)=='+' ||
                                postFix.charAt(k)=='-' ||
                                        postFix.charAt(k)=='*' ||
                                                postFix.charAt(k)=='/'
                                ){
//                            System.out.println("stcak count: "+ee.stack.size());
                            int op2=ee.values.pop();
                            int op1=ee.values.pop();
                               int  value= ee.evaluate(op1,op2,postFix.charAt(k));
//                               System.out.println("value: is:  "+value);
                               ee.values.push(value);
                        }
                        else{
                            ee.values.push(Character.getNumericValue(postFix.charAt(k)));
                        }
//                         System.out.println("char is: "+postFix.charAt(k));
                    }
                    
                    System.out.println("Answer for expression no. "+i+" is "+ee.values.pop());
                }
                }
            }

//            while ((sCurrentLine = br.readLine()) != null) {
//                ee.fullText += sCurrentLine;
//                System.out.println(sCurrentLine);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private boolean greater(char charAt, Character peek) {
        switch (charAt) {
            case '-':
                return false;

            case '/':
                return true;
            case '+':
                if (peek == '/' || peek == '*' || peek=='-') {
                    return false;
                } else {
                    return true;
                }

            case '*':
                if (peek == '/') {
                    return false;
                } else {
                    return true;
                }

        }
        return false;
    }

    private boolean less(char charAt, Character peek) {
        switch (charAt) {
            case '/':
                if(peek=='/')
                    return true;
                return false;//ok

            case '-':

                return true;

            case '+':
                if (peek == '/' || peek == '*' || peek=='+' || peek=='-') {
                    return true;
                }

            case '*':
                if (peek == '/' || peek=='*') {
                    return true;
                } else {
                    return false;
                }

        }
        return false;
    }
    private int evaluate(int op1,int op2,char x){


        switch(x){
            case '+':
            return op1+op2;
            
                case '-':
            return op1-op2;
            
                    case '*':
            return op1*op2;
            
                        case '/':
            return op1/op2;
            
        }
        return -1;
    }

}
