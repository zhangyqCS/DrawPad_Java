
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//帮助菜单功能的事项类
public class Help extends JFrame {
    private DrawPad drawpad = null;

    Help(DrawPad dp) {
        drawpad = dp;
    }

    public void MainHelp() {
        JOptionPane.showMessageDialog(this, "Draw_pad帮助文档", "Draw_pad", JOptionPane.WARNING_MESSAGE);
    }

    public void AboutBook() {
        JOptionPane.showMessageDialog(drawpad, "Draw_pad" + "\n" + "版本: 1.0" + "\n"
                + "作者:    " + "\n"
                + "      曹越 " + "\n"
        		+ "      张亚强" + "\n"
        		+ "      陈弘超" + "\n"
        		+ "      刘键" + "\n"
                + "完成时间:  2019/1月", "Draw_pad", JOptionPane.WARNING_MESSAGE);
    }
}
