package com.oopsconsultancy.xmltask.ant;

import com.oopsconsultancy.xmltask.*;

/**
 * the Ant call task
 *
 * @author <a href="mailto:brian@oopsconsultancy.com">Brian Agnew</a>
 * @version $Id$
 */
public class Call {

  private String path = null;
  private String target = null;
  private boolean inheritAll = true;
  private boolean inheritRefs = false;

  /**
   * executes a target for a set of nodes
   */
  public Call() {
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public void setInheritAll(boolean inheritAll) {
    this.inheritAll = inheritAll;
  }

  public void setInheritRefs(boolean inheritRefs) {
    this.inheritRefs = inheritRefs;
  }

  protected void process(XmlTask task) {
    if (path != null && target != null) {
      task.add(new XmlReplace(path, new CallAction(target, task, inheritAll, inheritRefs)));
    }
  }
}
