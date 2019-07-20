package org.java.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.java.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Transactional
    @Override
    public List<String> submitAndAssign(String processInstId){
        List<String> listname=new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processInstanceId(processInstId);
        List<Task> list = taskQuery.list();
        for (Task task: list) {
            String username=getTaskCandidate(task.getId());
            listname.add(username);
            task.setAssignee(username);
        }
        return listname;
    }

    //获取该任务的所有候选人
    public String getTaskCandidate(String taskId) {
        Set<User> users = new HashSet<User>();  //该任务的候选人
        Map<String,Integer> map=new HashMap<>();
        //查询权限
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if (identityLinksForTask != null && identityLinksForTask.size() > 0) {
            for (IdentityLink identityLink:identityLinksForTask) {
                if (identityLink.getUserId() != null) {
                    User user = getUser(identityLink.getUserId());
                    if (user != null)
                        users.add(user);
                }
                if (identityLink.getGroupId() != null) {
                    // 根据组获得对应人员
                    List<User> list = identityService.createUserQuery()
                            .memberOfGroup(identityLink.getGroupId()).list();
                    for (User user:list){
                        users.add(user);
                    }
                }
            }
        }
        for (User user:users){
            TaskQuery taskQuery = taskService.createTaskQuery();
            List<Task> list = taskQuery.taskAssignee(user.getFirstName()).list();
            map.put(user.getFirstName(),list.size());
        }
        Integer value=(Integer) map.values().toArray()[0];//获取map中第一个值，判断获得最小任务的用户
        String name=null;                   //最小任务的用户名
        for (String username : map.keySet()) {
            Integer i = map.get(name);
            if(i<value) {
                value=i;
                name=username;
            }
        }
        return name;
    }

    //根据一个用户ID查询出一个用户的信息
    private User getUser(String userId) {
        User user = (User) identityService.createUserQuery().userId(userId)
                .singleResult();
        return user;
    }


}
