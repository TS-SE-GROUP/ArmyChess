/*
 * 文件过滤器，用于过滤出ags后缀的游戏记录文件
 */

package armychess;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

//创建FileFilter的子类，用以实现文件过滤功能
class ExtensionFileFilter extends FileFilter
{

	private String description = "";
	private ArrayList<String> extensions = new ArrayList<String>();
	//自定义方法，用于添加文件扩展名
	public void addExtension(String extension)
	{
		if (!extension.startsWith("."))
		{
			extension = "." + extension;
			extensions.add(extension.toLowerCase());
		}
	}
	//用于设置该文件过滤器的描述文本
	public void setDescription(String aDescription)
	{
		description = aDescription;
	}
	//继承FileFilter类必须实现的抽象方法，返回该文件过滤器的描述文本
	public String getDescription()
	{
		return description; 
	}
	//继承FileFilter类必须实现的抽象方法，判断该文件过滤器是否接受该文件
	public boolean accept(File f)
	{
		//如果该文件是路径，接受该文件
		if (f.isDirectory()) return true;
		//将文件名转为小写（全部转为小写后比较，用于忽略文件名大小写）
		String name = f.getName().toLowerCase();
		//遍历所有可接受的扩展名，如果扩展名相同，该文件就可接受。
		for (String extension : extensions)
		{
			if (name.endsWith(extension)) 
			{
				return true;
			}
		}
		return false;
	}
}
