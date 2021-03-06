package com.oopsconsultancy.xmltask.ant;

import com.oopsconsultancy.xmltask.InsertAction;
import com.oopsconsultancy.xmltask.XmlReplace;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

import java.io.File;

/**
 * the Ant insertion task
 *
 * @author <a href="mailto:brian@oopsconsultancy.com">Brian Agnew</a>
 */
public class Insert implements Instruction {

  private XmlTask task = null;

  private String path = null;

  private String text = null; // text to insert (can be null)

  private InsertAction action = null;

  private InsertAction.Position position = InsertAction.Position.UNDER;

  private boolean expandProperties = true;

  /**
   * the buffer to insert
   */
  private String buffer;

  /**
   * the raw XML to insert
   */
  private String xml;

  /**
   * the file to insert
   */
  private File file;

  private String ifProperty;

  private String unlessProperty;

  public void setPath(String path) {
    this.path = path;
  }

  public void setPosition(String pos) {
    if ("before".equals(pos)) {
      position = InsertAction.Position.BEFORE;
    } else if ("after".equals(pos)) {
      position = InsertAction.Position.AFTER;
    } else if ("under".equals(pos)) {
      position = InsertAction.Position.UNDER;
    } else {
      log("Don't recognise position '" + pos + "'", Project.MSG_WARN);
    }
    if (action != null) {
      action.setPosition(position);
    }
  }

  private void log(final String msg, final int level) {
    if (task != null) {
      task.log(msg, level);
    } else {
      System.out.println(msg);
    }
  }

  public void setXml(final String xml) {
    this.xml = xml;
  }

  public void setFile(final File file) {
    this.file = file;
  }

  public void setExpandProperties(final boolean expandProperties) {
    this.expandProperties = expandProperties;
  }

  /**
   * used to insert literal text placed within the build.xml under the insert
   * element
   *
   * @param text String
   */
  public void addText(final String text) {
    this.text = text;
  }

  public void setBuffer(final String buffer) {
    this.buffer = buffer;
  }

  private void register() {
    try {
      if (xml != null) {
        action = InsertAction.fromString(xml, task);
      } else if (file != null) {
        action = InsertAction.fromFile(file, task);
      } else if (buffer != null) {
        action = InsertAction.fromBuffer(buffer, task);
      } else if (text != null) {
        if (expandProperties) {
          // we expand properties by default...
          text = PropertyHelper.getPropertyHelper(task.getProject()).replaceProperties(null, text, task.getProject().getProperties());
        }
        action = InsertAction.fromString(text, task);
      }
    } catch (Exception e) {
      throw new BuildException("Failed to add text to insert/paste", e);
    }
    if (action != null && path != null) {
      action.setPosition(position);
      XmlReplace xmlReplace = new XmlReplace(path, action);
      xmlReplace.setIf(ifProperty);
      xmlReplace.setUnless(unlessProperty);
      task.add(xmlReplace);
    }
  }

  public void process(final XmlTask task) {
    this.task = task;
    register();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.oopsconsultancy.xmltask.ant.Instruction#setIf(java.lang.String)
   */
  public void setIf(final String ifProperty) {
    this.ifProperty = ifProperty;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.oopsconsultancy.xmltask.ant.Instruction#setUnless(java.lang.String)
   */
  public void setUnless(final String unlessProperty) {
    this.unlessProperty = unlessProperty;
  }
}
