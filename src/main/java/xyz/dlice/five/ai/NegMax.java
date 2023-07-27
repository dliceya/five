package xyz.dlice.five.ai;

import com.alibaba.fastjson.JSON;

import java.util.Random;
import java.util.Scanner;

/**
 * 负极大值搜索
 */
public class NegMax {

    public static void main(String[] args) {
        int[][] chessBoard = new int[15][15];
        chessBoard[8][8] = 1;
        chessBoard[9][8] = -1;
        while (true) {
            NegMax play = new NegMax(1, chessBoard);
            int[] next = play.getNext(1);
            System.out.println(JSON.toJSONString(next));
            Scanner scanner = new Scanner(System.in);
            chessBoard[next[0]][next[1]] = -1;
            System.out.println(JSON.toJSONString(chessBoard));

            int x = scanner.nextInt();
            int y = scanner.nextInt();
            chessBoard[x][y] = 1;
        }
    }

    private static int searchDepth = 1;
    private final int robotColor;
    private final int boardLength = 14;

    private final int[][] chessBoard;

    public NegMax(int robotColor, int[][] chessBoard) {
        System.out.println(JSON.toJSONString(chessBoard));
        this.robotColor = robotColor;
        this.chessBoard = chessBoard;
    }

    public int[] getNext(int color) {
        int[] rel = new int[2];
        int maxScore = -100000000;

        Random random = new Random();

        for(int x=1; x<=boardLength; x++) {
            for(int y=1; y<=boardLength; y++) {

                if(chessBoard[x][y] != 0) continue;

                chessBoard[x][y] = color;

                int score = -alpha_betaFind(0, -100000000, 100000000, -color, x, y);

                int ra = random.nextInt(100);

                if(score > maxScore || (score == maxScore && ra >= 50)) {
                    maxScore = score;
                    rel[0] = x;
                    rel[1] = y;
                }

                chessBoard[x][y] = 0;
            }
        }
        System.out.println(maxScore);
        return rel;
    }

    public int alpha_betaFind(int depth, int alpha, int beta, int color, int prex, int prey) {

        if(depth >= searchDepth || 0 != this.isEnd(prex, prey, -color)) {
            int ans = this.reckon(robotColor) - this.reckon(-robotColor);

            if(depth % 2 == 0)
                ans = -ans;

            return ans;
        }

        for(int x=1; x<=boardLength; x++) {
            for(int y=1; y<=boardLength; y++) {

                if(chessBoard[x][y] != 0)
                    continue;

                chessBoard[x][y] = color;
                int val = -alpha_betaFind(depth+1, -beta, -alpha, -color, x, y);
                chessBoard[x][y] = 0;

                if(val >= beta)
                    return beta;

                if(val > alpha)
                    alpha = val;
            }
        }
        return alpha;
    }

    public int isEnd(int x, int y, int color) {
        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};

        for (int i = 0; i < 4; i++) {
            int sum = 1;

            int tx = x + dx[i];
            int ty = y + dy[i];
            while (tx > 0 && tx <= boardLength
                    && ty > 0 && ty <= boardLength
                    && chessBoard[tx][ty] == color) {
                tx += dx[i];
                ty += dy[i];
                ++sum;
            }

            tx = x - dx[i];
            ty = y - dy[i];
            while (tx > 0 && tx <= boardLength
                    && ty > 0 && ty <= boardLength
                    && chessBoard[tx][ty] == color) {
                tx -= dx[i];
                ty -= dy[i];
                ++sum;
            }

            if(sum >= 5)
                return color;
        }
        return 0;
    }

    public int reckon(int color) {

        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};
        int ans = 0;

        for(int x=1; x<=boardLength; x++) {
            for (int y = 1; y <= boardLength; y++) {
                if (chessBoard[x][y] != color)
                    continue;

                int[][] num = new int[2][100];

                for (int i = 0; i < 4; i++) {
                    int sum = 1;
                    int flag1 = 0, flag2 = 0;

                    int tx = x + dx[i];
                    int ty = y + dy[i];
                    while (tx > 0 && tx <= boardLength
                            && ty > 0 && ty <= boardLength
                            && chessBoard[tx][ty] == color) {
                        tx += dx[i];
                        ty += dy[i];
                        ++sum;
                    }

                    if(tx > 0 && tx <= boardLength
                            && ty > 0 && ty <= boardLength
                            && chessBoard[tx][ty] == 0)
                        flag1 = 1;

                    tx = x - dx[i];
                    ty = y - dy[i];
                    while (tx > 0 && tx <= boardLength
                            && ty > 0 && ty <= boardLength
                            && chessBoard[tx][ty] == color) {
                        tx -= dx[i];
                        ty -= dy[i];
                        ++sum;
                    }

                    if(tx > 0 && tx <= boardLength
                            && ty > 0 && ty <= boardLength
                            && chessBoard[tx][ty] == 0)
                        flag2 = 1;

                    if(flag1 + flag2 > 0)
                        ++num[flag1 + flag2 - 1][sum];
                }

                //成5⃣
                if(num[0][5] + num[1][5] > 0)
                    ans = Math.max(ans, 100000);
                    //活4 | 双死四 | 死4活3
                else if(num[1][4] > 0
                        || num[0][4] > 1
                        || (num[0][4] > 0 && num[1][3] > 0))
                    ans = Math.max(ans, 10000);
                    //双活3
                else if(num[1][3] > 1)
                    ans = Math.max(ans, 5000);
                    //死3活3
                else if(num[1][3] > 0 && num[0][3] > 0)
                    ans = Math.max(ans, 1000);
                    //死4
                else if(num[0][4] > 0)
                    ans = Math.max(ans, 500);
                    //单活3
                else if(num[1][3] > 0)
                    ans = Math.max(ans, 200);
                    //双活2
                else if(num[1][2] > 1)
                    ans = Math.max(ans, 100);
                    //死3
                else if(num[0][3] > 0)
                    ans = Math.max(ans, 50);
                    //双活2
                else if(num[1][2] > 1)
                    ans = Math.max(ans, 10);
                    //单活2
                else if(num[1][2] > 0)
                    ans = Math.max(ans, 5);
                    //死2
                else if(num[0][2] > 0)
                    ans = Math.max(ans, 1);
            }
        }

        return ans;
    }

}
