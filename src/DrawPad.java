
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

// 主界面类
public class DrawPad extends JFrame implements ActionListener {

    /**
     * @param FileName
     * DrawPad
     * @author 曹越 张亚强 陈弘超 刘键
     * @param V
     * 1.0.0
     */
    private static final long serialVersionUID = -2551980583852173918L;
    private JToolBar buttonpanel;//定义按钮面板
    private JMenuBar bar;//定义菜单条
    private JMenu file, color, stroke, help, edit;//定义菜单
    private JMenuItem newfile, openfile, savefile, exit;//file 菜单中的菜单项
    private JMenuItem helpin, helpmain, colorchoice, strokeitem;//help 菜单中的菜单项
    private JMenuItem editgraph, editcolor, editstroke, edittext;//编辑菜单中的选项
    private Icon nf, sf, of;//文件菜单项的图标对象
    private JLabel startbar;//状态栏
    private DrawArea drawarea;//画布类的定义
    private Help helpobject; //定义一个帮助类对象
    private FileClass fileclass;//文件对象
    String[] fontName;//字体名称

    private String names[] = {"newfile", "openfile", "savefile", "pen", "line",
            "rect", "frect", "oval", "foval", "circle",
            "fcircle", "roundrect", "froundrect", "word", "stroke",
            "delete", "move", "fill"};// 定义工具栏图标的名称

    private Icon icons[];//定义按钮图标数组

    private String tiptext[] = {//这里是鼠标移到相应的按钮上给出相应的提示
            "新建一个图片", "打开图片", "保存图片", "随笔画", "画直线",
            "画空心的矩形", "填充矩形", "画空心的椭圆", "填充椭圆", "画空心的圆",
            "填充圆", "画圆角矩形", "填充圆角矩形", "文字的输入", "选择线条的粗细",
            "删除一个图形", "移动图形", "填充图形"};

    JButton button[];//定义工具条中的按钮组
    private JCheckBox bold, italic;//工具条字体的风格（复选框）
    private JComboBox styles;//工具条中的字体的样式（下拉列表）

    DrawPad(String string) {
        //主界面的构造函数
        super(string);
        //菜单的初始化
        file = new JMenu("文件");
        edit = new JMenu("编辑");
        color = new JMenu("颜色");
        stroke = new JMenu("画笔");
        help = new JMenu("帮助");

        //菜单条的初始化
        bar = new JMenuBar();
        bar.add(file);//菜单条添加菜单
        bar.add(edit);
        bar.add(color);
        bar.add(stroke);
        bar.add(help);

        //界面中添加菜单条
        setJMenuBar(bar);

        //菜单中添加快捷键
        file.setMnemonic('F');//即ALT+“F”
        edit.setMnemonic('E');//即ALT+“E”
        color.setMnemonic('C');//即ALT+“C”
        stroke.setMnemonic('S');//即ALT+“S”
        help.setMnemonic('H');//即ALT+“H”

        //File菜单项的初始化
        
        nf = new ImageIcon("images/newfile.png");//创建图标
        sf = new ImageIcon("images/savefile.png");
        of = new ImageIcon("images/openfile.png");
        newfile = new JMenuItem("新建", nf);
        openfile = new JMenuItem("打开", of);
        savefile = new JMenuItem("保存", sf);
        exit = new JMenuItem("退出");

        //File菜单中添加菜单项
        file.add(newfile);
        file.add(openfile);
        file.add(savefile);
        file.add(exit);

        //File菜单项添加快捷键
        newfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        openfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        //File菜单项的注册监听
        newfile.addActionListener(this);
        openfile.addActionListener(this);
        savefile.addActionListener(this);
        exit.addActionListener(this);

        //Color菜单项的初始化
        colorchoice = new JMenuItem("调色板");
        colorchoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        colorchoice.addActionListener(this);
        color.add(colorchoice);

        //Help菜单项的初始化
        helpmain = new JMenuItem("帮助主题");
        helpin = new JMenuItem("关于Draw_pad");

        //Help菜单中添加菜单项
        help.add(helpmain);
        help.addSeparator();//添加分割线
        help.add(helpin);

        //Help菜单项的注册监听
        helpin.addActionListener(this);
        helpmain.addActionListener(this);

        //Stroke菜单项的初始化
        strokeitem = new JMenuItem("设置画笔");
        strokeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        stroke.add(strokeitem);
        strokeitem.addActionListener(this);

        //Edit菜单项初始化
        editgraph = new JMenuItem("编辑图形");
        editcolor = new JMenuItem("更改颜色");
        editstroke = new JMenuItem("更改线型");
        edittext = new JMenuItem("编辑文字");

        //Edit菜单中添加菜单项
        edit.add(editgraph);
        edit.add(editcolor);
        edit.add(editstroke);
        edit.add(edittext);

        //Edit菜单项注册监听
        editgraph.addActionListener(this);
        editcolor.addActionListener(this);
        editstroke.addActionListener(this);
        edittext.addActionListener(this);

        //工具栏的初始化
        buttonpanel = new JToolBar(JToolBar.VERTICAL);//垂直方向
        buttonpanel.setLayout(new GridLayout(15, 2, 0, 0));
        buttonpanel.setFloatable(false);//不可浮动
        icons = new ImageIcon[names.length];//初始化工具栏中的按钮
        button = new JButton[names.length];
        for (int i = 0; i < names.length; i++) {
            icons[i] = new ImageIcon("images/"+names[i] + ".png");//获得图片（以类路径为基准）
            button[i] = new JButton("", icons[i]);//创建工具条中的按钮
            button[i].setToolTipText(tiptext[i]);//鼠标移到相应的按钮上给出相应的提示
            buttonpanel.add(button[i]);
            button[i].setBackground(Color.white);
            if (i < 3) button[i].addActionListener(this);
            else if (i < names.length) button[i].addActionListener(this);
        }
        CheckBoxHandler CHandler = new CheckBoxHandler();//字体样式处理类
        bold = new JCheckBox("粗体");
        bold.setFont(new Font(Font.DIALOG, Font.BOLD, 30));//设置字体
        bold.addItemListener(CHandler);//bold注册监听
        italic = new JCheckBox("斜体");
        italic.addItemListener(CHandler);//italic注册监听
        italic.setFont(new Font(Font.DIALOG, Font.ITALIC, 30));//设置字体
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();//计算机上字体可用的名称
        fontName = ge.getAvailableFontFamilyNames();
        styles = new JComboBox(fontName);//下拉列表的初始化
        styles.addItemListener(CHandler);//styles注册监听
        styles.setMaximumSize(new Dimension(200, 50));//设置下拉列表的最大尺寸
        styles.setMinimumSize(new Dimension(100, 40));
        styles.setFont(new Font(Font.DIALOG, Font.BOLD, 20));//设置字体

        //添加字体式样栏
        JToolBar fontpanel = new JToolBar("字体选项", JToolBar.HORIZONTAL);//水平方向
        fontpanel.setLayout(new GridLayout(1, 3, 0, 0));
        fontpanel.setFloatable(false);//不可浮动
        fontpanel.add(bold);
        fontpanel.add(italic);
        fontpanel.add(styles);

        //状态栏的初始化
        startbar = new JLabel("Draw_pad");


        //选色区初始化
        Icon cicon = new ImageIcon("images/color.png");//自定义颜色按钮
        JButton cbutton = new JButton("", cicon);
        cbutton.setToolTipText("自定义颜色");
        cbutton.addActionListener(e -> drawarea.chooseColor());
        JButton colorButton[] = new JButton[20];//参考颜色按钮
        JToolBar colorButtonPanel = new JToolBar(JToolBar.VERTICAL);//参考颜色栏初始化
        colorButtonPanel.setLayout(new GridLayout(2, 15, 5, 5));
        colorButtonPanel.setFloatable(false);//不可浮动
        for (int i = 0; i < 20; i++) {
            colorButton[i] = new JButton("");//创建颜色栏中的按钮
            colorButtonPanel.add(colorButton[i]);
        }
        //20个参考颜色按钮初始化
        colorButton[0].setBackground(new Color(0xffffff));
        colorButton[1].setBackground(new Color(0x000000));
        colorButton[2].setBackground(new Color(0x848683));
        colorButton[3].setBackground(new Color(0xc3c3be));
        colorButton[4].setBackground(new Color(0xcdbedb));
        colorButton[5].setBackground(new Color(0xb97b56));
        colorButton[6].setBackground(new Color(0xffadd6));
        colorButton[7].setBackground(new Color(0xf01e1f));
        colorButton[8].setBackground(new Color(0x89010d));
        colorButton[9].setBackground(new Color(0xfef007));
        colorButton[10].setBackground(new Color(0xffc80c));
        colorButton[11].setBackground(new Color(0xff7c26));
        colorButton[12].setBackground(new Color(0xefe2ab));
        colorButton[13].setBackground(new Color(0xb6e51d));
        colorButton[14].setBackground(new Color(0x24b04f));
        colorButton[15].setBackground(new Color(0x93dceb));
        colorButton[16].setBackground(new Color(0x6997bb));
        colorButton[17].setBackground(new Color(0x07a0e6));
        colorButton[18].setBackground(new Color(0xd9a2dc));
        colorButton[19].setBackground(new Color(0x9c4ca1));
        for (int i = 0; i < 20; i++) {//给参考颜色按钮添加事件，按下时将当前颜色设置成与选中参考颜色相同
            int finalI = i;
            colorButton[i].addActionListener(e -> drawarea.colorBar(colorButton[finalI].getBackground().getRed(), colorButton[finalI].getBackground().getGreen(),
                    colorButton[finalI].getBackground().getBlue()));
        }

        //绘画区的初始化
        drawarea = new DrawArea(this);
        helpobject = new Help(this);
        fileclass = new FileClass(this, drawarea);


        Container con = getContentPane();//得到内容面板
        Toolkit tool = getToolkit();//得到一个Tolkit类的对象（主要用于得到屏幕的大小）
        Dimension dim = tool.getScreenSize();//得到屏幕的大小 （返回Dimension对象）
        con.setLayout(null);
        buttonpanel.setBounds(0, 0, 130, 1000);//给各工具栏安排位置
        startbar.setBounds(dim.width - 300, dim.height - 150, 300, 100);
        drawarea.setBounds(130, 0, dim.width - 10, dim.height - 200);
        colorButtonPanel.setBounds(130, dim.height - 180, 500, 100);
        fontpanel.setBounds(dim.width - 600, dim.height - 190, 450, 50);
        cbutton.setBounds(10, dim.height - 182, 100, 100);
        con.add(buttonpanel);//将各工具栏添加到主面板内
        con.add(drawarea);
        con.add(startbar);
        con.add(colorButtonPanel);
        con.add(fontpanel);
        con.add(cbutton);
        con.add(drawarea.tarea);
        setBounds(0, 0, dim.width, dim.height);
        setVisible(true);
        validate();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //设置状态栏显示的字符
    void setStratBar(String s) {
        startbar.setText(s);
    }

    //事件的处理
    public void actionPerformed(ActionEvent e) {
        for (int i = 3; i <= 13; i++) {//画各类图形
            if (e.getSource() == button[i]) {
                drawarea.setChosenStatus(i);
                drawarea.createNewitem();
                drawarea.repaint();
            }
        }
        if (e.getSource() == button[14] || e.getSource() == strokeitem) {
            drawarea.setStroke();//画笔粗细的调整
        } else if (e.getSource() == button[15]) {
            drawarea.setChosenStatus(15);//删除一个图形
        } else if (e.getSource() == button[16]) {
            drawarea.setChosenStatus(16);//拖动图形
        } else if (e.getSource() == editgraph) {
            drawarea.setChosenStatus(17);//改变已有图形大小
        } else if (e.getSource() == editcolor) {
            drawarea.setChosenStatus(18);//改变已有图形颜色
        } else if (e.getSource() == editstroke) {
            drawarea.setChosenStatus(19);//改变已有图形线型
        } else if (e.getSource() == button[17]) {
            drawarea.setChosenStatus(20);// 填充图片
        } else if (e.getSource() == edittext) {
            drawarea.setChosenStatus(21);//编辑已输入的文字
        } else if (e.getSource() == newfile || e.getSource() == button[0]) {
            fileclass.newFile();//新建
        } else if (e.getSource() == openfile || e.getSource() == button[1]) {
            fileclass.openFile();//打开
        } else if (e.getSource() == savefile || e.getSource() == button[2]) {
            fileclass.saveFile();//保存
        } else if (e.getSource() == exit) {
            System.exit(0);//退出程序
        } else if (e.getSource() == colorchoice) {
            drawarea.chooseColor();//颜色的选择
        } else if (e.getSource() == helpin) {
            helpobject.AboutBook();//帮助信息
        } else if (e.getSource() == helpmain) {
            helpobject.MainHelp();//帮助主题
        }
    }

    //字体样式处理类（粗体、斜体、字体名称）
    public class CheckBoxHandler implements ItemListener {

        public void itemStateChanged(ItemEvent ie) {
            if (ie.getSource() == bold)//字体粗体
            {
                if (ie.getStateChange() == ItemEvent.SELECTED)
                    drawarea.setFont(1, Font.BOLD);
                else
                    drawarea.setFont(1, Font.PLAIN);
            } else if (ie.getSource() == italic)//字体斜体
            {
                if (ie.getStateChange() == ItemEvent.SELECTED)
                    drawarea.setFont(2, Font.ITALIC);
                else drawarea.setFont(2, Font.PLAIN);

            } else if (ie.getSource() == styles)//字体的名称
            {
                drawarea.style = fontName[styles.getSelectedIndex()];
            }
        }

    }
}
