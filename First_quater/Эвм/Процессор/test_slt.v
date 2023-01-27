module test ();
  reg [4:0] a = 5'b11111, b = 5'b10001;
  reg [4:0] reg_res;
  always @(*) begin
    reg_res[4:1] = 5'b00000;
    reg_res[0] = a[4] != b[4] ? (a[4] > b[4] ? 1 : 0) : (a < b ? 1 : 0);
  end
  initial begin
    // Системная функция $display выводит текст на экран во время симуляции.
    $monitor("%d %d", a, b);
  end
endmodule