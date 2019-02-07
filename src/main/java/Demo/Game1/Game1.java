package Demo.Game1;

import com.sun.org.apache.xpath.internal.objects.XRTreeFragSelectWrapper;
import demo.Game1.Button;
import demo.Game1.Image;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;

/**
 * Tic Tac Toe Game
 * <p>
 * player first: x
 * computer: o
 */

public class Game1 extends JFrame implements ActionListener {
    private static final String PATH = "picture/images/";  //根据扑克代码没有改写成功图片的大小，在原图中改写的大小
    private static final int[][] results = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // row行
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // col列
            {0, 4, 8}, {2, 4, 6} // diagonal对角线
    };

    private JPanel main;
    private JButton[] buttons = new JButton[9];
    private boolean isGameOver;
    private int[] result;
    private int counter;

    /**
     * Constructor构造器
     */
    private Game1() {initComponent();}

    //初始化组件
    private void initComponent() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(100 * 3, 100 * 3);
        setLocationRelativeTo(null);

        main = new JPanel();
        main.setLayout(new GridLayout(3, 3));

        initButtons(main);

        add(main);
        setVisible(true);
    }

    //主面板初始化按钮
    //@param main 主面板
    private void initButtons(JPanel main) {
        for (int i = 0; i < buttons.length; i++) {
            JButton button = getButton();
            button.setBackground(null);
            button.setActionCommand(String.valueOf(i));
            buttons[i] = button;
            main.add(button);
        }
    }

    //获取 button
    //@return one button
        private JButton getButton() {
            JButton button = new JButton();
            button.setBorder(new LineBorder(Color.WHITE, 1));
            button.setOpaque(true);
            button.setBackground(Color.LIGHT_GRAY);
            button.addActionListener(this);
            return button;
        }

        //按钮事件响应
    //@param e 按钮事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOver) {
            newGame();
            return;
        }
        if (e.getActionCommand().matches("[xo]+")) {
            return;
        }

        // 玩家
        player(e);

        // AI
        ai();
    }

    //玩家下一步
    //@param e 按钮点击事件

    private void player(ActionEvent e) {
        JButton curr = (JButton) e.getSource();
        curr.setIcon(new ImageIcon(PATH + "1.jpg"));
        int currIndex = Integer.parseInt(e.getActionCommand());
        buttons[currIndex].setActionCommand("x");
        counter++;

        printCurrent();

        checkGameOver();

        if (isGameOver) {
            System.out.println("--- game over ---");
            showResult();
        }
    }

    /**
     * 打印当前棋局
     */
    private void printCurrent() {
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print("\t" + button.getActionCommand());
        }
        System.out.println();
    }

    /**
     * 初始化新游戏
     */

    private void newGame() {
        setTitle("Tic Tac Toe");
        main.removeAll();
        initButtons(main);
        main.revalidate();
        main.repaint();
        isGameOver = false;
        result = new int[3];
        counter = 0;
    }

    /**
     * 显示游戏结果
     * <p>
     * who is winner
     * by which line
     */
    private void showResult() {
        if (result != null) {
            String s = buttons[result[0]].getActionCommand();
            for (int i : result) {
                buttons[i].setBackground(Color.red);
            }
            if (s.equals("x")) {
                //setTitle("玩家胜");将标题显示结果改为出现对话框显示结果
                JOptionPane.showMessageDialog(main, "恭喜你获胜！", "结果", JOptionPane.PLAIN_MESSAGE);

            } else {
                //setTitle("AI 胜");
                JOptionPane.showMessageDialog(main, "AI获胜", "结果", JOptionPane.PLAIN_MESSAGE);

            }
        } else {
            //setTitle("平局");
            JOptionPane.showMessageDialog(main, "平局", "结果", JOptionPane.PLAIN_MESSAGE);

        }
    }

    /**
     * 判断游戏是否结束
     */
    private void checkGameOver() {
        for (int[] result : results) {
            if (isLine(result)) {
                isGameOver = true;
                this.result = result;
                return;
            }
        }
        if (counter == 9) {
            isGameOver = true;
            this.result = null;
        }
    }

    /**
     * 判断是否有三子成一线
     *
     * @param result 可能成一线的三子下标
     * @return 是否有三子成一线
     */
    private boolean isLine(int[] result) {
        String aString = buttons[result[0]].getActionCommand();
        String bString = buttons[result[1]].getActionCommand();
        String cString = buttons[result[2]].getActionCommand();
        return aString.equals(bString) && bString.equals(cString);
    }

    private String getString(int i) {
        return buttons[i].getActionCommand();
    }

    /**
     * 计算机 AI
     */
    private void ai() {
        if (isGameOver) {
            return;
        }
        int position = -1;
        // TODO: 21/01/2019 to be optimized
        // 在 indexes 中寻找最优
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < buttons.length; i++) {
            if (!buttons[i].getActionCommand().matches("[xo]+")) {
                indexes.add(i);
            }
        }
        if (indexes.size() > 0) {
            position = indexes.get(new Random().nextInt(indexes.size()));
        }
        // TODO: 21/01/2019 to be optimized
        // 列举所有二缺一，优先选自己
        for (int[] ints : results) {
            if (getString(ints[0]).equals(getString(ints[1]))) {
                if (!buttons[ints[2]].getActionCommand().matches("[xo]+")) {
                    position = ints[2];
                }
            } else if (getString(ints[1]).equals(getString(ints[2]))) {
                if (!buttons[ints[0]].getActionCommand().matches("[xo]+")) {
                    position = ints[0];
                }
            } else if (getString(ints[0]).equals(getString(ints[2]))) {
                if (!buttons[ints[1]].getActionCommand().matches("[xo]+")) {
                    position = ints[1];
                }
            }
        }
        if (position != -1) {
            buttons[position].setIcon(new ImageIcon(PATH + "2.jpg"));
            buttons[position].setActionCommand("o");
            counter++;
            printCurrent();
        }

        checkGameOver();

        if (isGameOver) {
            System.out.println("--- game over ---");
            showResult();
        }
    }

    public static void main(String[] args) {
        new Game1();
    }
}


