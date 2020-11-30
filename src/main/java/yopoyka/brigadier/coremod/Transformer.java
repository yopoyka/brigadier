package yopoyka.brigadier.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

import static yopoyka.brigadier.coremod.Asm.*;

public class Transformer implements IClassTransformer {
    protected static Logger log = LogManager.getLogger("Brigadier Core Plugin");
    protected Map<String, IClassTransformer> transformers = new HashMap<>();
    {
        final String hooks = "yopoyka/brigadier/coremod/Hooks";
        transformers.put("net.minecraft.network.NetHandlerPlayServer", (name, transformedName, basicClass) -> {
            final ClassNode classNode = read(basicClass);

//            addInterface(classNode, "yopoyka/brigadier/coremod/IServerHandler");
//            classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "br_hasBrigadier", "Z", null, null));
//
//            classNode.methods.add(initMethodCode(createMethod("hasBrigadier()Z"), compose(
//                    getThis(),
//                    getField(classNode.name, "br_hasBrigadier", "Z"),
//                    addInst(Opcodes.IRETURN)
//            )));
//
//            classNode.methods.add(initMethodCode(createMethod("setHasBrigadier(Z)V"), compose(
//                    getThis(),
//                    accessVar(Opcodes.ILOAD, 1),
//                    setField(classNode.name, "br_hasBrigadier", "Z"),
//                    addInst(Opcodes.RETURN)
//            )));

            classNode.methods
                    .stream()
                    .filter(forMethod(GradleMcp.instance.fromSrg("func_147341_a")))
                    .filter(forMethodDesc("(Lnet/minecraft/network/play/client/C14PacketTabComplete;)V"))
                    .findFirst()
                    .ifPresent(methodNode -> {
                        log.info("transforming processTabComplete {}", methodNode.name);

                        insertFirst(methodNode.instructions, supplyCode(compose(
                                getThis(),
                                accessVar(Opcodes.ALOAD, 1),
                                callStatic(hooks, "processTabComplete", "(Lnet/minecraft/network/NetHandlerPlayServer;Lnet/minecraft/network/play/client/C14PacketTabComplete;)V"),
                                addInst(Opcodes.RETURN)
                        )).get());
                    });

            return write(classNode, new ClassWriter(0));
        });
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformers.isEmpty()) return basicClass;

        final IClassTransformer transformer = transformers.remove(transformedName);
        if (transformer != null)
            return transformer.transform(name, transformedName, basicClass);

        return basicClass;
    }
}
