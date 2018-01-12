package com.dotridge.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dotridge.bean.LoginBean;
import com.dotridge.bean.RoleBean;
import com.dotridge.bean.UserProfileBean;
import com.dotridge.service.RegistrationService;
import com.dotridge.service.RoleService;
import com.dotridge.util.ServiceConstants;


@Controller
public class HomeController 
{
	/*@Autowired
	public LoginValidator loginValidator;*/
	
	/*@InitBinder
	public void initBinder(WebDataBinder binder) 
	{
	  binder.setValidator(loginValidator);
	}*/
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="/getHomePage")
	public String getHomePage()
	{
		return "home";
	}
	
	
	@RequestMapping(value="/getLoginPage")
	public String getLoginPage(Model model)
	{
		model.addAttribute("loginBean", new LoginBean());
		return "login";
	}
	
	@RequestMapping(value="/login")
	public String doLogin(@Valid @ModelAttribute("loginBean")LoginBean loginBean,BindingResult result)
	{
		if(result.hasErrors())
		{
			List<FieldError> errorList = result.getFieldErrors();
			for(FieldError fieldError : errorList)
			{
				System.out.println(fieldError);
			}
		}
		System.out.println(loginBean.toString());
		String userName = loginBean.getUserId();
		if (userName != null && !userName.isEmpty()) 
		{
		   if (userName.equalsIgnoreCase("superadmin@email.com"))
		   {
			   return "getSuperAdminBoard";
		   }
		   else
		   {
			   return "home";
		   }
		}
		else
		{
			return "home";
		}
	}
	
	@RequestMapping("/formLogin")
	public String formLogin(HttpServletRequest request)
	{
		Authentication authonticationHeader = SecurityContextHolder.getContext().getAuthentication();
		String username = authonticationHeader.getName();
		Collection<? extends GrantedAuthority> authList = authonticationHeader.getAuthorities();
		
		String role = null;
		
		for(GrantedAuthority auth:authList) {
			System.out.println(auth.getAuthority());
			role = auth.getAuthority();
		}
		/*String[] headers = (String)authonticationHeader.split(" ");
		String token = headers[1];
		String decode = new String(Base64.getDecoder().decode(token));
		String userName = decode.split(":")[0];
		System.out.println(userName);*/
		
		/*String userName = request.getParameter("name");
		String password = request.getParameter("password");	
		System.out.println(userName);*/
		
		//System.out.println(username);
		
		if(role != null && !role.isEmpty())
		{
			if(role.equalsIgnoreCase(ServiceConstants.ROLE_SUPER_ADMIN))
			{
				return "getSuperAdminBoard";
			}
		}
		return "home";
	}
	
	
	@RequestMapping("/windowLogin")
	public String windowLogin(HttpServletRequest request)
	{
		Authentication authonticationHeader = SecurityContextHolder.getContext().getAuthentication();
		String username = authonticationHeader.getName();
		/*String[] headers = (String)authonticationHeader.split(" ");
		String token = headers[1];
		String decode = new String(Base64.getDecoder().decode(token));
		String userName = decode.split(":")[0];
		System.out.println(userName);*/
		
		/*String userName = request.getParameter("name");
		String password = request.getParameter("password");
		System.out.println(userName);*/
		
		System.out.println(username);
		
		if(username != null && !username.isEmpty())
		{
			if(username.equalsIgnoreCase("superadmin@email.com"))
			{
				return "getSuperAdminBoard";
			}
		}
		return "home";
	}
	
	@RequestMapping(value="/getSignUpPage")
	public String getSignUpPage(Model model)
	{
		UserProfileBean userProfileBean = new UserProfileBean();
		model.addAttribute("userProfileBean",userProfileBean);
		
		List<RoleBean> roleBeans = roleService.getRoles();
		Set<String> roles = new HashSet<String>();
		if(roleBeans != null && !roleBeans.isEmpty())
		{
			for(RoleBean roleBean : roleBeans)
			{
				roles.add(roleBean.getRoleName());
			}
		}
		model.addAttribute("rolesList",roles);
		return "registration";
	}
	
	@RequestMapping(value="/registration")
	public String doSignUp(@ModelAttribute("userProfileBean") UserProfileBean userProfileBean)
	{
		System.out.println("In regstartion");
		UserProfileBean user = registrationService.createUser(userProfileBean);
		System.out.println(user.toString());
		if(user != null)
		{
			return "login";
		}
		else
		{
			return "registration";
		}
	}
}
