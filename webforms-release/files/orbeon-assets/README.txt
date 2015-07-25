The locations to deploy the files are different depending on the scope that you want for such files
	WEB-INF/resources/forms/assets: scripts for all forms
	WEB-INF/resources/forms/APP/assets: scripts for app name APP
	WEB-INF/resources/forms/APP/FORM/assets: scripts for app name APP and form name FORM


THIS CONFIGURATION AFFECTS ALL THE ORBEON FORMS DEPLOYED IN THE SAME SERVER
The folder '/assets' must be in the folder '/WEB-INF/resources/forms/' of the orbeon war.

The following line must be in the 'properties-local-xml' file of the orbeon '/WEB-INF/resources/config/' folder
	<!-- Set the css to use -->
	<property as="xs:string" name="oxf.fr.css.custom.uri.*.*"  value="/forms/assets/BiiT.css"/>

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++										 ¡¡¡¡ IMPORTANT !!!!
++		The wizard style must be deactivated for the categories menu to work. 
++
++ 		You MUST comment or delete the following lines if they exist:
++ 			<!-- Configure wizard layout -->
++ 			<property as="xs:string" name="oxf.fr.detail.view.appearance.*.*" value="wizard"/>
++ 			<property as="xs:string" name="oxf.fr.detail.buttons.*.*" value="wizard-prev wizard-next submit"/>
++ 
++ 		You MUST also comment or delete the following line related to the form from the properties local (if exists)
++     		<property as="xs:string" name="oxf.fr.detail.buttons.WebForms.FORM_BEING_USED" value="wizard-prev wizard-next send"/>
++    
++ 		Our wizard uses the old orbeon submit button, instead of the new 'send' button, so you have to change this line:
++    		<property as="xs:string" name="oxf.fr.detail.process.send.*.*" value="require-valid then save-final then send('oxf.fr.detail.send.success')"/>
++ 		into this line:
++     		<property as="xs:string" name="oxf.fr.detail.process.submit.*.*" value="require-valid then save-final then send('oxf.fr.detail.send.success')"/>
++     
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

TASK #38: Change Orbeon Label of repeatable groups
Only need to edit the properties locale and add the line:
	<property as="xs:string"  name="oxf.fr.resource.*.*.en.components.grid.insert-below" value='Add new record'/>
	

