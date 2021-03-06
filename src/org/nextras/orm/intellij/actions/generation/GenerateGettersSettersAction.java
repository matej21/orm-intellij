package org.nextras.orm.intellij.actions.generation;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;


public class GenerateGettersSettersAction extends CodeInsightAction
{
	private final GenerateActionHandler actionHandler = new GenerateActionHandler ()
	{
		@Override
		protected boolean canShow(PhpDocProperty property, PhpClass phpClass)
		{
			String name = property.getName();
			String getter_methodName = "getter" + name.substring(0, 1).toUpperCase() + name.substring(1);
			String setter_methodName = "setter" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Method getter_method = phpClass.findMethodByName(getter_methodName);
			Method setter_method = phpClass.findMethodByName(setter_methodName);
			return getter_method == null && setter_method == null;
		}

		@Override
		protected String createAccessors(Field field, Project project)
		{
			String name = field.getName();

			String getter_methodName = "getter" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Method getter_method = PhpPsiElementFactory.createMethod(project, "protected function " + getter_methodName + "($value) { return $value; }");

			String setter_methodName = "setter" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Method setter_method = PhpPsiElementFactory.createMethod(project, "protected function " + setter_methodName + "($value) { return $value; }");

			return setter_method.getText() + '\n' + getter_method.getText();
		}
	};


	public GenerateGettersSettersAction()
	{
	}


	@Override
	protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file)
	{
		return this.actionHandler.isValidForFile(project, editor, file);
	}


	@NotNull
	@Override
	protected CodeInsightActionHandler getHandler()
	{
		return this.actionHandler;
	}
}
