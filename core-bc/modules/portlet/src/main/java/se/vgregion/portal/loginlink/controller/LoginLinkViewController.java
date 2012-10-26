package se.vgregion.portal.loginlink.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.liferay.expando.UserExpandoHelper;
import se.vgregion.liferay.expando.UserExpandoHelperFactory;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Patrik Bergström
 */
@Controller
@RequestMapping(value = "VIEW")
public class LoginLinkViewController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginLinkViewController.class.getName());

    @Value("${login.internal.url}")
    private String internalLoginUrl;
    @Value("${login.external.url}")
    private String externalLoginUrl;

    @Value("${ip_for_external_access}")
    private String ipForExternalAccess;

    public LoginLinkViewController(){

    }
	
    @RenderMapping()
    public String showLoginLink(RenderRequest request) {

        Boolean isInternalAccess = internalAccessRule(PortalUtil.getHttpServletRequest(request));

        if (isInternalAccess) {
            request.setAttribute("loginUrl", internalLoginUrl);
        } else {
            request.setAttribute("loginUrl", externalLoginUrl);
        }

        return "view";
    }

    private User getUser(PortletRequest request) {
        return ((ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY)).getUser();
    }

    private boolean internalAccessRule(HttpServletRequest request) {
        String header = request.getHeader("x-forwarded-for");
        String[] ipsForExternalAccess = ipForExternalAccess.replaceAll(" ", "").split(",");
        boolean internal = true;
        if (header != null) {
            // Iterate over the ip:s. We'll find a match if the user is located externally.
            for (String ip : ipsForExternalAccess) {
                if (header.contains(ip)) { // String.contains(...) since the header value may be a comma-separated list.
                    return false;
                }
            }
        }
        return true;
    }


}
