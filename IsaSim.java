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
import java.nio.file.Files;

public class IsaSim {

	static int pc;
	static int reg[] = new int[4];

	// Here the first program hard coded as an array
	// static int progr[] = {
	// 		// As minimal RISC-V assembler example
	// 		0x00200093, // addi x1 x0 2
	// 		0x00300113, // addi x2 x0 3
	// 		0x002081b3, // add x3 x1 x2
	// };
	static int progr[];

	public static void main(String[] args) {

		int j = 0;

		// File file = new File("./tests/task1/addlarge.s");
		// try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	 //    	String line;
  //   		while ((line = br.readLine()) != null) {
  //   			progr[j++] = line;
  //    	  		System.out.println(line);
  //   		}
		// }
		// catch(FileNotFoundException ex) {
  //           System.out.println("Unable to open file");                
  //       }
  //       catch(IOException ex) {
  //           System.out.println("Error reading file");                  
  //           // ex.printStackTrace();
  //       }

		File file = new File("./tests/task1/addlarge.bin");
		throws IOException {
			byte[] fileCont = Files.readAllBytes(file.toPath());
		}

		System.out.println("Hello RISC-V World!");

		pc = 0;

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

			//jal
			case 0x6f:
				rd = (instr >> 7) & 0x01f;
				imm = (instr >> 12);
			
			//jalr
			case 0x67:
				rd = (instr >> 7) & 0x01f;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);
				
			case 0x63:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20) & 0x01f;
				imm = (instr >> 25);
				
				//beq
				if (funct == 0x0) {

				}

				//bne
				if (funct == 0x1) {
					
				}

				//blt
				if (funct == 0x4) {
					
				}

				//bge
				if (funct == 0x5) {
					
				}

				//bltu
				if (funct == 0x6) {
					
				}

				//bgeu
				if (funct == 0x7) {
					
				}				
			
			case 0x03:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x007;
				rs1 = (instr >> 15) & 0x01f;
				imm = (instr >> 20);

				//lb
				if (funct == 0x0) {

				}
					
				//lh
				if (funct == 0x1) {

				}
					
				//lw
				if (funct == 0x2) {

				}
					
				//lbu
				if (funct == 0x4) {

				}
					
				//lhu
				if (funct == 0x5) {

				}

			case 0x23:
				rd = (instr >> 7) & 0x01f;
				funct = (instr >> 12) & 0x07;
				rs1 = (instr >> 15) & 0x01f;
				rs2 = (instr >> 20) & 0x01f;
				imm = (instr >> 25);

				//sb
				if (funct == 0x0) {

				}
					
				//sh
				if (funct == 0x1) {

				}
					
				//sw
				if (funct == 0x2) {

				}


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
					
				if (funct == 0x5) {
					//srli
					if (funct7 == 0x0)

					//srai
					else if (funct7 == 0x20)

				}
				

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
