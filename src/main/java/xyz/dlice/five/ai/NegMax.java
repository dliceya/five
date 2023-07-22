package xyz.dlice.five.ai;

import java.util.Random;

/**
 * 负极大值搜索
 */
public class NegMax {

    private int boardLength;
    private int[][] chessBoard;

    public NegMax () {

    }

    /* 返回AI走法 */
    public int[] getNext(int color) {
        int[] rel = new int[2];
        int ans = -100000000;

        Random random = new Random();

        for(int x=1; x<=boardLength; x++) {
            for(int y=1; y<=boardLength; y++) {

                if(chessBoard[x][y] != 0)
                    continue;

                chess.makeMove(x, y, color);

                int val = -alpha_betaFind(0, -100000000, 100000000, color%2 + 1, x, y);
                //System.out.printf("x=%d, y=%d, val=%d\n", x, y, val);

                int ra = random.nextInt(100);

                if(val > ans || val == ans && ra >= 50) {
                    ans = val;
                    rel[0] = x;
                    rel[1] = y;
                }

                chess.unMove(x, y);
            }
        }
        System.out.println(ans);
        return rel;
    }


}
