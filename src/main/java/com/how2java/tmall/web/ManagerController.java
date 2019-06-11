package com.how2java.tmall.web;

import com.how2java.tmall.dao.ManagerDAO;
import com.how2java.tmall.pojo.Manager;
import com.how2java.tmall.service.ManagerSerive;
import com.how2java.tmall.util.Page4Navigator;
import com.how2java.tmall.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class ManagerController {
    @Autowired
    ManagerSerive managerSerive;
    /*
    @PostMapping("/managerlogin")
    public Object login(@RequestBody Manager managerParam, HttpSession session) {
        String name =  managerParam.getName();
        name = HtmlUtils.htmlEscape(name);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, managerParam.getPassword());
        try {
            subject.login(token);
            Manager manager = managerSerive.getByName(name);
            session.setAttribute("manager",manager);
            return Result.success();
        } catch (AuthenticationException e) {
            String message ="账号密码错误";
            return Result.fail(message);
        }
    }*/

    @PostMapping("/managerlogin")
    public Object login(@RequestBody Manager managerParam, HttpSession session){
        String name =  managerParam.getName();
        name = HtmlUtils.htmlEscape(name);

        Manager manager=managerSerive.get(name,managerParam.getPassword());
        if(null==manager){
            String message ="账号密码错误";
            return Result.fail(message);
        }
        else{
            session.setAttribute("manager", manager);
            return Result.success();
        }
    }
    @GetMapping("/managercheck")
    public Object check(HttpSession session)
    {
        Manager manager= (Manager) session.getAttribute("manager");
        int power=manager.getPower();
        if(power!=1)
        {
            return Result.fail("无权访问");
        }
        return Result.success();
    }
    @GetMapping("/managers")
    public Page4Navigator<Manager> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size){
        start = start<0?0:start;
        Page4Navigator<Manager> page = managerSerive.list(start,size,5);
        return page;
    }
    @PostMapping("/managers")
    public Object add(Manager bean) throws Exception {
        managerSerive.add(bean);
        return bean;
    }
    @GetMapping("/managerlogout")
    public Object logout(HttpSession session){
        session.removeAttribute("manager");
        return null;
    }
    /*
    @GetMapping("/testmanagers")
    public Object add(@RequestParam(value = "name")String name,@RequestParam(value = "password")String password)
    {


        name = HtmlUtils.htmlEscape(name);


        boolean exist = managerSerive.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }


        int times = 2;
        String algorithmName = "md5";
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        Manager manager=new Manager();
        manager.setName(name);
        manager.setPassword(encodedPassword);
        manager.setPower(1);
        manager.setSalt(salt);
        managerSerive.add(manager);

        return Result.success();
    }*/
}
