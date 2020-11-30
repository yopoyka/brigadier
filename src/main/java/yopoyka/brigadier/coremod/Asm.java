/* full version at
* https://github.com/yopoyka/code-snippets/blob/master/src/main/java/yopoyka/mctool/Asm.java
*/
package yopoyka.brigadier.coremod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.function.*;

public class Asm {
    public static ClassNode read(byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        return classNode;
    }

    public static byte[] write(ClassNode classNode, ClassWriter cw) {
        classNode.accept(cw);
        return cw.toByteArray();
    }

    public static Predicate<MethodNode> forMethod(String name) {
        return m -> name.equals(m.name);
    }

    public static Predicate<MethodNode> forMethodDesc(String desc) {
        return m -> desc.equals(m.desc);
    }

    public static Consumer<InsnList> callMethod(int opcode, String owner, String name, String desc) {
        return list -> list.add(new MethodInsnNode(
                opcode,
                owner,
                name,
                desc,
                opcode == Opcodes.INVOKEINTERFACE
        ));
    }

    public static Consumer<InsnList> callStatic(String owner, String name, String desc) {
        return callMethod(Opcodes.INVOKESTATIC, owner, name, desc);
    }

    public static Consumer<InsnList> accessVar(int opcode, int index) {
        return list -> list.add(new VarInsnNode(opcode, index));
    }

    public static Consumer<InsnList> getThis() {
        return list -> list.add(new VarInsnNode(Opcodes.ALOAD, 0));
    }

    public static Consumer<InsnList> addInst(int opcode) {
        return list -> list.add(new InsnNode(opcode));
    }

    public static <T> Consumer<T> compose(Consumer<T>... consumers) {
        return t -> {
            for (Consumer<T> consumer : consumers)
                consumer.accept(t);
        };
    }

    public static void addInterface(ClassNode classNode, String inter) {
        if (!classNode.interfaces.contains(inter))
            classNode.interfaces.add(inter);
    }

    public static void insertFirst(InsnList list, InsnList insert) {
        list.insertBefore(list.getFirst(), insert);
    }

    public static InsnList createCode(Consumer<InsnList> init) {
        InsnList list = new InsnList();
        init.accept(list);
        return list;
    }

    public static Supplier<InsnList> supplyCode(Consumer<InsnList> init) {
        return () -> createCode(init);
    }
}
