package org.ssssssss.script.parsing.ast.binary;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.functions.DynamicModuleImport;
import org.ssssssss.script.functions.ObjectTypeConditionExtension;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.BinaryOperation;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.runtime.handle.OperatorHandle;

import java.io.Serializable;

/**
 * instanceof
 */
public class InstanceofOperation extends BinaryOperation {

    public InstanceofOperation(Expression leftOperand, Span span, Expression rightOperand) {
        super(leftOperand, span, rightOperand);
    }

    @Override
    public void compile(MagicScriptCompiler compiler) {
        compiler.visit(getLeftOperand())
                .visit(getRightOperand())
                .typeInsn(CHECKCAST, Class.class)
                .lineNumber(getSpan())
                .invoke(INVOKESTATIC, ObjectTypeConditionExtension.class, "is", boolean.class, Object.class, Class.class)
                .asBoolean();
    }

}
