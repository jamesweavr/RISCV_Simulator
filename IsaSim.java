/**
 * RISC-V Instruction Set Simulator
 * 
 * A tiny first step to get the simulator started. Can execute just a single
 * RISC-V instruction.
 * 
 * @author Martin Schoeberl (martin@jopdesign.com)
 *
 */
public class IsaSim {

	static int pc;
	static int reg[] = new int[4];

	// Here the first program hard coded as an array
	static int progr[] = {
			// As minimal RISC-V assembler example
			0x00200093, // addi x1 x0 2
			0x00300113, // addi x2 x0 3
			0x002081b3, // add x3 x1 x2
	};

	public static void main(String[] args) {

		System.out.println("Hello RISC-V World!");

		pc = 0;

		for (;;) {

			int instr = progr[pc];
			int opcode = instr & 0x7f;
			int rd, rs1, rs2, imm;

			switch (opcode) {

			case 0x13:
				rd = (instr >> 7) & 0x01f;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);
				reg[rd] = reg[rs1] + imm;
				break;
			case 0x33:
				rd = (instr >> 7) & 0x01f;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20);
				reg[rd] = reg[rs1] + reg[rs2];
				break;
			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}

			for (int i = 0; i < reg.length; ++i) {
				System.out.print(reg[i] + " ");
			}

			System.out.println();

			++pc; // We count in 4 byte words
			if (pc >= progr.length) {
				break;
			}
		}

		System.out.println("Program exit");

	}

}
