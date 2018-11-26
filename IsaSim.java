/**
 * RISC-V Instruction Set Simulator
 * 
 * A tiny first step to get the simulator started. Can execute just a single
 * RISC-V instruction.
 * 
 * @author Martin Schoeberl (martin@jopdesign.com)
 *
 *
 * en.wikichip.org/wiki/risc-v/registers
 * rv8.io/isa.html
 */

import java.io.*;
import java.nio.file.Files;

public class IsaSim {

	static int pc;
	static int ra;
	static int reg[] = new int[31];

	public static final String FIN = 
	//"./tests/task1/addneg.bin";
	//"./tests/task2/branchmany.bin";
	//"./tests/task3/loop.bin";
	//"./tests/InstructionTests/test_jalr.bin";
	"./tests/FinalAssignmentTests/t14.bin";

	public static final String FOUT = "out.bin";

	// Here the first program hard coded as an array
	// static int progr[] = {
	// 		// As minimal RISC-V assembler example
	// 		0x00200093, // addi x1 x0 2
	// 		0x00300113, // addi x2 x0 3
	// 		0x002081b3, // add x3 x1 x2
	// };
	static int progr[];

	public static void main(String[] args) {
		File fileName = new File(FIN);

		byte[] buff = new byte[1000];
		try {
			buff = Files.readAllBytes(fileName.toPath());
		} catch (IOException e) {
			System.out.println("Io Exception " + e);
		}

		progr = new int[buff.length / 4];
   		int offset = 0;
		for(int i = 0; i < progr.length; i++) {
			progr[i] = (buff[0 + offset] & 0xFF) | ((buff[1 + offset] & 0xFF) << 8) |
				((buff[2 + offset] & 0xFF) << 16) | ((buff[3 + offset] & 0xFF) << 24);  
			offset += 4;
			System.out.println(progr[i]);
		}


		System.out.println("Hello RISC-V World!");

		pc = 0;
		ra = 0;

		for (;;) {

			int instr = progr[pc];
			int opcode = instr & 0x7f;
			int rd, funct, funct7, rs1, rs2, imm;

			//System.out.println(opcode);

			switch (opcode) {
			//lui
			case 0x37:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
				reg[rd] = (imm << 12);
				break;

			//auipc
			case 0x17:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
				reg[rd] = pc + (imm << 12);
				//*******Plus offset********
				break;

			//jal
			case 0x6f:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
				int bit1 = (imm & 0x7fe00) >> 9;
				int bit2 = (imm & 0x100) << 2;
				int bit3 = (imm & 0xff) << 12;
				int bit4 = (imm >> 19) << 19;

				imm = bit1 | bit2 | bit3 | bit4;

				//the offset is sign extended and added to the pc
				System.out.println(imm);
				reg[rd] = pc + 1; 
				pc = pc + imm;
				break;
			
			//jalr
			case 0x67:
				rd = (instr >> 7) & 0x01f;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);

				//Target address is obtained by adding the 12 bit signed imm to the register rs1 
				//then setting the least significant bit to 0
				reg[rd] = pc + 1;
				pc = pc + (((rs1 + imm) & 0xFFE)/4);
				break;

			case 0x63:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x07;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20) & 0x01f;
				imm = (instr >> 25);
				imm = rd | (imm << 5);
				System.out.println(imm);

				if (imm < 0)
					imm--;
				imm = imm/4;

				//System.out.println("Imm is " + imm + " Pc is " + pc);

				//beq
				if (funct == 0x0) {
					//System.out.println("beq");
					if (rs1 == rs2)
						pc = pc + imm - 1;
				}

				//bne
				if (funct == 0x1) {
					//System.out.println("bne");
					if (reg[rs1] != reg[rs2])
						pc = pc + imm - 1;
 				}

				//blt
				if (funct == 0x4) {
					//System.out.println("blt");
					if (reg[rs1] < reg[rs2])
						pc = pc + imm - 1;
				}

				//bge
				if (funct == 0x5) {
					//System.out.println("bge");
					if (reg[rs1] > reg[rs2])
						pc = pc + imm - 1;
				}

				//bltu
				if (funct == 0x6) {
					if (reg[rs1] < reg[rs2])
						pc = pc + imm - 1;
				}

				//bgeu
				if (funct == 0x7) {
					if (reg[rs1] > reg[rs2])
						pc = pc + imm - 1;					
				}

				//System.out.println(rs1 + "(" +reg[rs1] + ") = " + rs2 + "(" + reg[rs2] + ")");

				break;				
			
			case 0x03:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);

				//cast to 32 bit
				reg[rd] = reg[rs1 + (imm >> 20)];

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

				imm = rd | (imm << 5);
				System.out.println(rs1);
				System.out.println(imm/4);
				System.out.println(rs2);

				reg[rs1 + (imm/4)] = reg[rs2];

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
				if (funct == 0x0) {
					reg[rd] = reg[rs1] + (imm);
				}
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

				//System.out.println(rd +"("+reg[rd] + ") = " + rs1 +"("+ reg[rs1]+") + " + imm);


				break;

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

			case 0x73:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x07;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);
				//ecall
			
				break;

			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}



			try {
				FileOutputStream fostream = new FileOutputStream(FOUT, false);
				ObjectOutputStream oostream = new ObjectOutputStream(fostream);

				System.out.print("pc" + pc + " ");
				for (int i = 0; i < 31; ++i) {
					oostream.writeInt(reg[i]);
					System.out.print(reg[i] + " ");
				}
				System.out.println();

				oostream.close();

			} catch (FileNotFoundException e) {
				System.out.println("File not found " + e);
			} catch (IOException e) {
				System.out.println("IOException " + e);
			}







			++pc; // We count in 4 byte words
			if (pc >= progr.length) {
				break;
			}
		}

		System.out.println("Program exit");

	}

}
