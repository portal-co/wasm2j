package io.github.eutro.wasm2j.passes.opts;

import io.github.eutro.wasm2j.ext.CommonExts;
import io.github.eutro.wasm2j.ops.CommonOps;
import io.github.eutro.wasm2j.passes.InPlaceIrPass;
import io.github.eutro.wasm2j.ssa.Insn;
import io.github.eutro.wasm2j.ssa.Var;

import java.util.ListIterator;

public class IdentityElimination implements InPlaceIrPass<Insn> {
    public static final IdentityElimination INSTANCE = new IdentityElimination();

    @Override
    public void runInPlace(Insn insn) {
        ListIterator<Var> iter = insn.args.listIterator();
        while (iter.hasNext()) {
            Var arg = iter.next();
            arg.getExt(CommonExts.ASSIGNED_AT).ifPresent(assigned -> {
                if (assigned.insn().op.key != CommonOps.IDENTITY.key) return;
                if (assigned.getAssignsTo().size() != 1) return;
                if (assigned.insn().args.size() != 1) return;
                iter.set(assigned.insn().args.get(0));
            });
        }
    }
}
