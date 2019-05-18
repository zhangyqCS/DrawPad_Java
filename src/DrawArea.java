
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;


//绘图区类（各种图形的绘制和鼠标事件）
public class DrawArea extends JPanel {
    DrawPad drawpad = null;
    Drawing[] itemList = new Drawing[5000];// 绘制图形及相关参数全部存到该数组

    int chooseni = 0;// 当前选中图形的数组下标
    int x0, y0;//记录移动图形鼠标起始位置
    private int chosenStatus = 3;//设置默认基本图形状态为随笔画
    int index = 0;//当前已经绘制的图形数目
    private Color color = Color.black;//当前画笔的颜色
    int R, G, B;//用来存放当前颜色的彩值
    int f1, f2;//用来存放当前字体的风格
    String style;// 存放当前字体
    float stroke = 1.0f;//设置画笔的粗细 ，默认的是 1.0
    JTextArea tarea = new JTextArea("");
    int tx, ty;

    DrawArea(DrawPad dp) {
        drawpad = dp;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        // 把鼠标设置成十字形
        setBackground(Color.white);// 设置绘制区的背景是白色
        addMouseListener(new MouseA());// 添加鼠标事件
        addMouseMotionListener(new MouseB());
        createNewitem();
    }

    public void paintComponent(Graphics g) {// repaint()需要调用
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int j = 0;
        while (j <= index) {
            draw(g2d, itemList[j]);
            j++;
        } // 将itemList数组重画一遍
    }

    void draw(Graphics2D g2d, Drawing i) {
        i.draw(g2d);// 将画笔传到个各类的子类中
    }

    //新建一个图形的基本单元对象的程序段
    void createNewitem() {
        if (chosenStatus == 13)// 字体的输入光标相应的设置为文本输入格式
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        else setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        switch (chosenStatus) {// button触发改变currentChoice的值，由此得到各事件的入口
            case 3:
                itemList[index] = new Pencil();
                break;
            case 4:
                itemList[index] = new Line();
                break;
            case 5:
                itemList[index] = new Rect();
                break;
            case 6:
                itemList[index] = new fillRect();
                break;
            case 7:
                itemList[index] = new Oval();
                break;
            case 8:
                itemList[index] = new fillOval();
                break;
            case 9:
                itemList[index] = new Circle();
                break;
            case 10:
                itemList[index] = new fillCircle();
                break;
            case 11:
                itemList[index] = new RoundRect();
                break;
            case 12:
                itemList[index] = new fillRoundRect();
                break;
            case 13:
                itemList[index] = new Word();
                break;
        }
        if (chosenStatus >= 3 && chosenStatus <= 13) {
            itemList[index].type = chosenStatus;
            itemList[index].R = R;
            itemList[index].G = G;
            itemList[index].B = B;
            itemList[index].stroke = stroke;
        }

    }

    public void setIndex(int x) {//设置index的接口
        index = x;
    }

    public int getIndex() {// 读取index的接口
        return index;
    }

    public void setColor(Color color)//设置颜色的值
    {
        this.color = color;
    }

    public void setStroke(float f)//设置画笔粗细的接口
    {
        stroke = f;
    }

    public void chooseColor()//选择当前颜色
    {
        color = JColorChooser.showDialog(drawpad, "请选择颜色", color);
        try {
            R = color.getRed();
            G = color.getGreen();
            B = color.getBlue();
        } catch (Exception e) {
            R = 0;
            G = 0;
            B = 0;
        }
        itemList[index].R = R;
        itemList[index].G = G;
        itemList[index].B = B;
    }

    public void colorBar(int cR, int cG, int cB) {
        R = cR;
        G = cG;
        B = cB;
        itemList[index].R = R;
        itemList[index].G = G;
        itemList[index].B = B;
    }

    public void changeColor()// 改变当前图片的颜色
    {
        color = JColorChooser.showDialog(drawpad, "请选择颜色", color);
        try {
            R = color.getRed();
            G = color.getGreen();
            B = color.getBlue();
        } catch (Exception e) {
            R = 0;
            G = 0;
            B = 0;
        }
        itemList[chooseni].R = R;
        itemList[chooseni].G = G;
        itemList[chooseni].B = B;
    }

    public void setStroke()//画笔粗细的调整
    {
        String input;
        input = JOptionPane.showInputDialog("请输入画笔的粗细( >0 )");
        try {
            stroke = Float.parseFloat(input);

        } catch (Exception e) {
            stroke = 1.0f;

        }
        itemList[index].stroke = stroke;

    }

    public void changeStroke()// 画笔粗细的改变（主要针对空心图形、直线、随笔画）
    {
        String input;
        input = JOptionPane.showInputDialog("请输入画笔的粗细( >0 )");
        try {
            stroke = Float.parseFloat(input);

        } catch (Exception e) {
            stroke = 1.0f;

        }
        itemList[chooseni].stroke = stroke;

    }

    public void setChosenStatus(int i)// 设置当前选择（button触发时使用）
    {
        chosenStatus = i;
    }

    public void changeText() {//修改已有文字
        String input;
        input = JOptionPane.showInputDialog("请输入你要修改为的文字");
        itemList[chooseni].s1 = input;//重设选中文本框的各参数
        itemList[chooseni].type = f1 + f2;
        itemList[chooseni].s2 = style;
        itemList[chooseni].stroke = stroke;
        itemList[chooseni].R = R;
        itemList[chooseni].G = G;
        itemList[chooseni].B = B;
    }

    public void setFont(int i, int font)//设置字体
    {
        if (i == 1) {
            f1 = font;
        } else
            f2 = font;
    }

    public void fillColor(Drawing nowdrawing) {// 填充
        int choice = nowdrawing.gettypechoice();// 用于判断填充图形类型
        if (choice == 5) {
            itemList[chooseni] = new fillRect();
        } else if (choice == 7) {
            itemList[chooseni] = new fillOval();
        } else if (choice == 9) {
            itemList[chooseni] = new fillCircle();
        } else if (choice == 11) {
            itemList[chooseni] = new fillRoundRect();
        }
        itemList[chooseni].x1 = nowdrawing.x1;
        itemList[chooseni].x2 = nowdrawing.x2;
        itemList[chooseni].y1 = nowdrawing.y1;
        itemList[chooseni].y2 = nowdrawing.y2;
        itemList[chooseni].R = R;
        itemList[chooseni].G = G;
        itemList[chooseni].B = B;
    }

    public void deletePaint(Drawing nowdrawing) {// 删除
        int choice = nowdrawing.gettypechoice();
        if (choice >= 3 && choice <= 13) {
            itemList[chooseni] = new Line();
        }
    }

    // 鼠标事件MouseA类继承了MouseAdapter，用来完成鼠标的响应事件的操作
    class MouseA extends MouseAdapter {
        public void mouseEntered(MouseEvent me) {
            // 鼠标进入
            drawpad.setStratBar("鼠标进入在：[" + me.getX() + " ," + me.getY() + "]");// 设置状态栏提示
        }

        public void mouseExited(MouseEvent me) {
            // 鼠标退出
            drawpad.setStratBar("鼠标退出在：[" + me.getX() + " ," + me.getY() + "]");
        }

        public void mousePressed(MouseEvent me) {
            // 鼠标按下
            drawpad.setStratBar("鼠标按下在：[" + me.getX() + " ," + me.getY() + "]");
            if (chosenStatus >= 15 && chosenStatus <= 21)
            // 删除，移动，更改大小，更改颜色，更改线型，填充六种操作都需要选定图形
            {
                for (chooseni = index - 1; chooseni >= 0; chooseni--) {
                    // 从后到前寻找当前鼠标是否在某个图形内部
                    if (itemList[chooseni].in(me.getX(), me.getY())) {
                        if (chosenStatus == 16)// 移动图形需要记录press时的坐标
                        {
                            x0 = me.getX();
                            y0 = me.getY();
                        }
                        break;// 其它操作只需找到currenti即可
                    }
                }
                if (chooseni >= 0) {// 有图形被选中
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 更改鼠标样式为手形
                    if (chosenStatus == 20) {// 触发填充
                        fillColor(itemList[chooseni]);
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));// 鼠标样式变回十字花
                        repaint();
                    } else if (chosenStatus == 15) {// 触发删除
                        deletePaint(itemList[chooseni]);
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        repaint();
                    } else if (chosenStatus == 18) {// 改变已有图形的颜色
                        changeColor();
                        repaint();
                    } else if (chosenStatus == 19) {// 改变已有图形的线型
                        changeStroke();
                        repaint();
                    } else if (chosenStatus == 21) {//改变已有文字
                        changeText();
                        repaint();
                    }
                }
            } else {
                itemList[index].x1 = itemList[index].x2 = me.getX();
                itemList[index].y1 = itemList[index].y2 = me.getY();// x1,x2,y1,y2初始化
                // 如果当前选择为随笔画则进行下面的操作
                if (chosenStatus == 3) {
                    itemList[index].x1 = itemList[index].x2 = me.getX();
                    itemList[index].y1 = itemList[index].y2 = me.getY();
                    index++;
                    createNewitem();//创建新的图形的基本单元对象
                }
                //如果选择图形的文字输入，则进行下面的操作
                if (chosenStatus == 13) {
                    tx = me.getX();
                    ty = me.getY();
                    tarea.setBounds(tx, ty, 0, 0);
                    tarea.setBorder(new LineBorder(new java.awt.Color(127, 157, 185), 1, false));
                }
            }
        }

        public void mouseReleased(MouseEvent me) {
            // 鼠标松开
            drawpad.setStratBar("鼠标松开在：[" + me.getX() + " ," + me.getY() + "]");
            if (chosenStatus == 16) {// 移动结束

                if (chooseni >= 0) {// 鼠标成功选择了某个图形
                    itemList[chooseni].x1 = itemList[chooseni].x1 + me.getX() - x0;
                    itemList[chooseni].y1 = itemList[chooseni].y1 + me.getY() - y0;
                    itemList[chooseni].x2 = itemList[chooseni].x2 + me.getX() - x0;
                    itemList[chooseni].y2 = itemList[chooseni].y2 + me.getY() - y0;
                    repaint();
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            } else if (chosenStatus == 17) {// 放大缩小结束
                if (chooseni >= 0) {// 鼠标成功选择了某个图形
                    itemList[chooseni].x2 = me.getX();
                    itemList[chooseni].y2 = me.getY();
                    repaint();
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            } else {
                if (chosenStatus == 3) {// 随笔画绘制结束
                    itemList[index].x1 = me.getX();
                    itemList[index].y1 = me.getY();
                } else if (chosenStatus == 13) {// 文本框绘制结束
                    tarea.setBounds(Math.min(tx, me.getX()) + 130, Math.min(ty, me.getY()), Math.abs(tx - me.getX()), Math.abs(ty - me.getY()));//绘制文本框
                    String input;
                    input = JOptionPane.showInputDialog("请输入你要写入的文字");
                    tarea.setText(input);
                    itemList[index].s1 = input;
                    itemList[index].type = f1 + f2;//设置粗体、斜体
                    itemList[index].x2 = me.getX();
                    itemList[index].y2 = me.getY();
                    itemList[index].s2 = style;//设置字体

                    index++;
                    chosenStatus = 13;
                    createNewitem();//创建新的图形的基本单元对象
                    repaint();
                    tarea.setText("");//重设文本框，为下一次使用做准备
                    tarea.setBounds(tx, ty, 0, 0);
                }
                if (chosenStatus >= 3 && chosenStatus <= 12) {
                    itemList[index].x2 = me.getX();
                    itemList[index].y2 = me.getY();
                    repaint();
                    index++;
                    createNewitem();//创建新的图形的基本单元对象
                }
            }
        }
    }

    // 鼠标事件MouseB继承了MouseMotionAdapter，用来处理鼠标的滚动与拖动
    class MouseB extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me)//鼠标的拖动
        {
            drawpad.setStratBar("鼠标拖动在：[" + me.getX() + " ," + me.getY() + "]");
            if (chosenStatus == 3) {// 任意线的画法
                itemList[index - 1].x1 = itemList[index].x2 = itemList[index].x1 = me.getX();
                itemList[index - 1].y1 = itemList[index].y2 = itemList[index].y1 = me.getY();
                index++;
                createNewitem();//创建新的图形的基本单元对象
                repaint();
            } else if (chosenStatus == 16) {
                if (chooseni >= 0) {// 移动的过程
                    itemList[chooseni].x1 = itemList[chooseni].x1 + me.getX() - x0;
                    itemList[chooseni].y1 = itemList[chooseni].y1 + me.getY() - y0;
                    itemList[chooseni].x2 = itemList[chooseni].x2 + me.getX() - x0;
                    itemList[chooseni].y2 = itemList[chooseni].y2 + me.getY() - y0;
                    x0 = me.getX();
                    y0 = me.getY();
                    repaint();
                }
            } else if (chosenStatus == 17) {// 放大缩小的过程
                if (chooseni >= 0) {
                    itemList[chooseni].x2 = me.getX();
                    itemList[chooseni].y2 = me.getY();
                    repaint();
                }
            } else if (chosenStatus >= 3 && chosenStatus <= 12) {// 绘制图形的过程
                itemList[index].x2 = me.getX();
                itemList[index].y2 = me.getY();
                repaint();
            }
            //repaint();
        }

        public void mouseMoved(MouseEvent me)//鼠标的移动
        {
            drawpad.setStratBar("鼠标移动在：[" + me.getX() + " ," + me.getY() + "]");
            for (chooseni = index - 1; chooseni >= 0; chooseni--) {
                // 从后到前寻找当前鼠标是否在某个图形内部
                if (itemList[chooseni].in(me.getX(), me.getY())) {
                    break;// 其它操作只需找到currenti即可
                }
            }
            if (chooseni >= 0) {// 有图形被选中
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// 更改鼠标样式为箭头
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
        }
    }

}
