package com.biit.webforms.gui;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.form.interfaces.IBaseFormView;
import com.biit.webforms.authentication.ApplicationController;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.liferay.portal.model.User;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}
	
	private static HashMap<Form, User> formsInUse = new HashMap<Form, User>(); 

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
	private static List<ApplicationController> controllers = new ArrayList<ApplicationController>();

	public static synchronized void register(ApplicationController controller) {
		controllers.add(controller);
	}

	public static synchronized void unregister(ApplicationController controller) {
		controllers.remove(controller);
		controller.freeLockedResources();
	}

	public static synchronized void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(final String message) {
		for (final BroadcastListener listener : listeners)
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listener.receiveBroadcast(message);
				}
			});
	}

	public static synchronized List<SimpleEntry<User, Form>> getFormsInUseData() {
		List<SimpleEntry<User, Form>> formsInUseData = new ArrayList<SimpleEntry<User, Form>>();
		for (ApplicationController controller : controllers) {
			User user = controller.getUser();
			Form form = controller.getFormInUse();
			if (user != null && form != null) {
				formsInUseData.add(new SimpleEntry<User, Form>(user, form));
			}
		}
		return formsInUseData;
	}
	
	public static synchronized boolean isUserUserUsingForm(User user, IWebformsFormView form){
		return formsInUse.get(form)!=null && formsInUse.get(form).equals(user);
	}
	
	public static synchronized User getUserUsingForm(IBaseFormView form){
		return formsInUse.get(form);
	}
	
	public static synchronized void lockForm(Form form, User user){
		if(form == null || user == null){
			return;
		}
		
		if(!formsInUse.containsKey(form)){
			WebformsLogger.info(UiAccesser.class.getName(), "User '"+user.getEmailAddress()+"' has locked '"+form+"'");
			formsInUse.put(form,user);
		}
	}
	
	public static synchronized void releaseForm(Form form, User user){
		WebformsLogger.info(UiAccesser.class.getName(), "User '"+user.getEmailAddress()+"' has released '"+form+"'");
		//If form is still locked and the user is who lock the form.
		if(formsInUse.containsKey(form) && formsInUse.get(form).equals(user)){
			formsInUse.remove(form);
		}
	}
}
