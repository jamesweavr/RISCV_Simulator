/**
 * RISC-V Instruction Set Simulator
 * 
 * A tiny first step to get the simulator started. Can execute just a single
 * RISC-V instruction.
 * 
 * @author Martin Schoeberl (martin@jopdesign.com)
 *
 */

import java.io.*;
import java.nio.*;
import java.nio.file.Files;

public class IsaSim {

	static int pc;
	static int ra;
	static int reg[] = new int[5];

	// Here the first program hard coded as an array
	// static int progr[] = {
	// 		// As minimal RISC-V assembler example
	// 		0x00200093, // addi x1 x0 2
	// 		0x00300113, // addi x2 x0 3
	// 		0x002081b3, // add x3 x1 x2
	// };
	static int progr[];

	public static void main(String[] args) {

		File fileName = new File("./tests/task1/addlarge.bin");

		try {
			FileInputStream fileIs = new FileInputStream(fileName);
			ObjectInputStream is = new ObjectInputStream(fileIs);
			int i = 0;
			while (true) {
				int imp;
				try {
					imp = is.readInt();
				} catch (EOFException e) {
					System.out.println("Here");
					break;
				} 
				System.out.println(imp);
				//progr[i++] = imp;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Io Exception " + e); 
		}








		System.out.println("Hello RISC-V World!");

		pc = 0;
		ra = 0;

		for (;;) {

			int instr = progr[pc];
			int opcode = instr & 0x7f;
			int rd, funct, funct7, rs1, rs2, imm;

			//rv8.io/isa.html
			switch (opcode) {

			//lui
			case 0x37:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
				reg[rd] = imm;
				break;

			//auipc
			case 0x17:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
				reg[rd] = imm;
				//*******Plus offset********
				break;

			//jal
			case 0x6f:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);

				//the offset is sign extended and added to the pc
				pc = pc + imm*4;
				ra = rd;
				break;
			
			//jalr
			case 0x67:
				rd = (instr >> 7) & 0x01f;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);

				//Target address is obtained by adding the 12 bit signed imm to the register rs1 
				//then setting the least significant bit to 0
				pc = pc + (rs1 + imm);
				ra = rd;
				break;

			case 0x63:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x07;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20) & 0x01f;
				imm = (instr >> 25);
				
				//beq
				if (funct == 0x0) {
					if (rs1 == rs2)
						pc = pc + imm;
				}

				//bne
				if (funct == 0x1) {
					if (rs1 != rs2)
						pc = pc + imm;
 				}

				//blt
				if (funct == 0x4) {
					if (rs1 < rs2)
						pc = pc + imm;
				}

				//bge
				if (funct == 0x5) {
					if (rs1 > rs2)
						pc = pc + imm;
				}

				//bltu
				if (funct == 0x6) {
					if (rs1 < rs2)
						pc = pc + imm;
				}

				//bgeu
				if (funct == 0x7) {
					if (rs1 > rs2)
						pc = pc + imm;					
				}

				break;				
			
			case 0x03:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);

				//cast to 32 bit
				reg[rd] = reg[rs1 + imm];

				// //lb
				// if (funct == 0x0)
				// 	reg[rs1 + imm] = reg[rd] << 24 >> 24;
					
				// //lh
				// if (funct == 0x1)
				// 	reg[rs1 + imm] = reg[rd] << 16 >> 16;
					
				// //lw
				// if (funct == 0x2)
				// 	reg[rs1 + imm] = reg[rd];
					
				// //lbu
				// if (funct == 0x4) {
				// 	reg[rs1 + imm] = reg[rd] << 24 >> 24;

				// //lhu
				// if (funct == 0x5) {
				// 	reg[rs1 + imm] = reg[rd] << 16 >> 16;


				break;

			case 0x23:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x07;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20) & 0x01f;
				imm = (instr >> 25);

				//cast to 32 bit
				reg[rs1 + imm] = reg[rs2];

				// //sb
				// if (funct == 0x0) {

				// }
					
				// //sh
				// if (funct == 0x1) {

				// }
					
				// //sw
				// if (funct == 0x2) {

				// }

				break;

			//addi
			case 0x13:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);
				funct7 = (instr >> 25);

				//addi
				if (funct == 0x0)
					reg[rd] = reg[rs1] + imm;

				//slti
				if (funct == 0x2) {
					if (reg[rs1] < imm)
						reg[rd] = 1;
					else
						reg[rd] = 0;
				}

				//sltiu
				if (funct == 0x3) {
					if (reg[rs1] < imm)
						reg[rd] = 1;
					else
						reg[rd] = 0;
				}

				//xori
				if (funct == 0x4)
					reg[rd] = reg[rs1] ^ imm;

				//ori
				if (funct == 0x6)
					reg[rd] = reg[rs1] | imm;

				//andi
				if (funct == 0x7)
					reg[rd] = reg[rs1] & imm;

				//slli
				if (funct == 0x1)
					reg[rs1] = reg[rs1] << imm;

				if (funct == 0x5) {
					//srli
					if (funct7 == 0x0)
						reg[rs1] = reg[rs1] >> imm;
					//srai
					else if (funct7 == 0x20)
						reg[rs1] = reg[rs1] >> imm;						
				}

				break;

			//add
			case 0x33:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20);
				funct7 = (instr >> 25);

				//add sub
				if (funct == 0x0) {
					//add
					if (funct7 == 0x0)
						reg[rd] = reg[rs1] + reg[rs2];

					//sub
					else if (funct == 0x20)
						reg[rd] = reg[rs1] - reg[rs2];
				}


				//sll
				if (funct == 0x1) 
					reg[rd] = reg[rs1] << reg[rs2];

				//slt
				if (funct == 0x2) {
					if (reg[rs1] < reg[rs2])
						reg[rd] = 1;
					else
						reg[rd] = 0;
				}

				//sltu
				if (funct == 0x3) {
					if (reg[rs1] < reg[rs2])
						reg[rd] = 1;
					else
						reg[rd] = 0;
				}

				//xor
				if (funct == 0x4)
					reg[rd] = reg[rs1] ^ reg[rs2];

				if (funct == 0x5) {
					//srl
					if (funct7 == 0x0)
						reg[rd] = reg[rs1] >> reg[rs2];

					//sra
					else if (funct == 0x20)
						reg[rd] = reg[rs1] >> reg[rs2];
				}

				//or
				if (funct == 0x6)
					reg[rd] = reg[rs1] | reg[rs2];

				//and
				if (funct == 0x7)
					reg[rd] = reg[rs1] & reg[rs2];

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
