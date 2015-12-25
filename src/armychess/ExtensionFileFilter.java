/*
 * �ļ������������ڹ��˳�ָ����׺����Ϸ��¼�ļ�
 */

package armychess;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

//����FileFilter�����࣬����ʵ���ļ����˹���
class ExtensionFileFilter extends FileFilter
{

	private String description = "";
	private ArrayList<String> extensions = new ArrayList<String>();
	//�Զ��巽������������ļ���չ��
	public void addExtension(String extension)
	{
		if (!extension.startsWith("."))
		{
			extension = "." + extension;
			extensions.add(extension.toLowerCase());
		}
	}
	//�������ø��ļ��������������ı�
	public void setDescription(String aDescription)
	{
		description = aDescription;
	}
	//�̳�FileFilter�����ʵ�ֵĳ��󷽷������ظ��ļ��������������ı�
	public String getDescription()
	{
		return description; 
	}
	//�̳�FileFilter�����ʵ�ֵĳ��󷽷����жϸ��ļ��������Ƿ���ܸ��ļ�
	public boolean accept(File f)
	{
		//������ļ���·�������ܸ��ļ�
		if (f.isDirectory()) return true;
		//���ļ���תΪСд��ȫ��תΪСд��Ƚϣ����ں����ļ�����Сд��
		String name = f.getName().toLowerCase();
		//�������пɽ��ܵ���չ���������չ����ͬ�����ļ��Ϳɽ��ܡ�
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
