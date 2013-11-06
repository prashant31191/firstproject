package com.qinnuan.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qinnuan.engine.bean.film.Seat;
import com.qinnuan.engine.R;
import com.qinnuan.common.http.image.util.ImageFetcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SeatWidget extends LinearLayout implements
        TouchView.SeatViewOperationListener {
    public interface OnSeatSelected {
        public void onSeatSelected(View view, Seat seat, boolean hasChoice);
    }

    private OnSeatSelected onSeatSelected;
    private TouchView seatView;
    private RelativeLayout numberLayout;
    private int width;
    private int height;
    private List<Seat> list;
    private ImageView iv;
    private Bitmap bitmap;

    public SeatWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.seat_layout, this);
        width = context.getResources().getDimensionPixelSize(R.dimen.seat_size);
        height = context.getResources()
                .getDimensionPixelSize(R.dimen.seat_size);
        seatView = (TouchView) findViewById(R.id.seatview);
        seatView.setOperationListener(this);
        numberLayout = (RelativeLayout) findViewById(R.id.numberLayout);
        numberLayout.setOnTouchListener(null);
        iv = (ImageView) findViewById(R.id.screenImage);
        if (Build.VERSION.SDK_INT > 10) {
            bitmap = ((BitmapDrawable) (iv.getDrawable())).getBitmap();

            iv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    if (arg1.getX() > 0 && arg1.getY() > 0) {
                        if (bitmap.getPixel((int) (arg1.getX()), ((int) arg1.getY())) == 0) {
//                        Log.i("Test", "透明区域");
                            return false;//透明区域返回true
                        }
                    } else {
                        return true;
                    }
                    return true;
                }
            });
        } else {
            iv.setVisibility(GONE);
        }
    }

    public void setOnSeatSelected(OnSeatSelected onSeatSelected) {
        this.onSeatSelected = onSeatSelected;
    }

    @Override
    public void seatOnClick(View view, Seat seat, boolean hasChoice) {
        if (onSeatSelected != null) {
            onSeatSelected.onSeatSelected(view, seat, hasChoice);
        }
    }

    @Override
    public void initNumber(List<Seat> list, int height) {
        numberLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            Seat seat = list.get(i);
            MarginLayoutParams mParams = new MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT);
            mParams.setMargins(0, seat.getRownum() * height, 0, 0);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    mParams);
            TextView text = new TextView(getContext());
            text.setText(seat.getRowid());
            text.setLayoutParams(params);
            numberLayout.addView(text);
        }
    }


    @Override
    public void scroll(int y) {
        numberLayout.scrollBy(0, y);
    }

    public View getSeatView(Seat seat) {
        for (int i = 0; i < seatView.getChildCount(); i++) {
            Seat s = (Seat) seatView.getChildAt(i).getTag();
            if (seat.getRowid().equals(s.getRowid())
                    && seat.getColumnid().equals(s.getColumnid())) {
                return seatView.getChildAt(i);
            }
        }
        return null;
    }

    public void setDate(List<Seat> list) {
        this.list = list;
        seatView.clearChoiceList();
        seatView.initView(list, width, height);
        for (Seat seat : list) {
            seat.setMySelect(false);
        }
//        seatView.initViewS(list,width,height);
    }

    public void setDateWithHead(List<Seat> list, ImageFetcher fetcher) {
        this.list = list;
        seatView.initView(list, width, height);
        seatView.drawHead(fetcher);
        seatView.clearChoiceList();
//        seatView.initViewS(list,width,height);
    }

    public List<Seat> getChoiceList() {
        return seatView.getChoiceList();
    }

    private boolean seatIsNull() {
        boolean seatIsNull = true;
        if (seatView != null) {
            if (seatView.getChoiceList().size() >= 1) {
                seatIsNull = false;
            }
        }

        return seatIsNull;
    }

    /**
     * 检查所选座位是否合法
     *
     * @return
     */


    public boolean checkSeat() {
        if (seatIsNull()) {
            Toast.makeText(getContext(), "请选择座位", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Seat seat : seatView.getChoiceList()) {
            if (check(seat)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean check(Seat seat) {
        Queue<SeatNode> queue = new LinkedList<SeatNode>();
        //将需要判断的座位加入队列
        for (int i = 0; i < list.size(); i++) {
            Seat s = list.get(i);
            if (!s.isLostSeat()) continue;
            if (s.getRownum() == seat.getRownum()) {
                SeatNode node = new SeatNode();
                node.seat = s;
                node.pre = getSeatByRowAndCol(s.getRownum(), s.getColumnnum() - 1);
                node.next = getSeatByRowAndCol(s.getRownum(), s.getColumnnum() + 1);
                if (node.pre != null && node.pre.isLostSeat()) continue;
                if (node.next != null && node.next.isLostSeat()) continue;
                if (node.pre != null && node.pre.isSystemSeat() && node.next == null) continue;
                if (node.next!= null && node.next.isSystemSeat() && node.pre== null) continue;
                if ((node.pre != null && node.pre.isSystemSeat()) && (node.next != null && node.next.isSystemSeat()))
                    continue;
                queue.add(node);
            }
        }
        //没有被选择的空座位进行判断
        while (!queue.isEmpty()) {
            SeatNode node = queue.poll();
            if ((node.pre != null && node.pre.isLostSeat()) || (node.next != null && node.next.isLostSeat())) {//前面或者后面有位置没有被选择
//               return true ;
                continue;
            }
            if (node.pre == null && node.next == null) {
                continue;
            }
            if (node.pre == null) {//左边无位置
                if (node.next.isLostSeat()) {
                    continue;
                } else if(rightContinue(node.seat)){
                    continue;
                }else{
                    return false;
                }
            }
            if (node.next == null) {//右边无位置
                if (node.pre.isLostSeat()) {
                    continue;
                } else if(leftContinue(node.seat)){
                    continue;
                }else{
                    return false;
                }
            }
            if (!node.pre.isLostSeat() && !node.next.isLostSeat()) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }
    //左边连续
   public boolean leftContinue(Seat seat){
       for (int i=seat.getColumnnum()-1;i>0;i--){
            Seat s=getSeatByRowAndCol(seat.getRownum(),i) ;
           if(s==null){//左边无位置
                return true;
           }else{
                 if(s.isLostSeat()){
                     return false;
                 }
           }
       }
       return  true ;
   }
    //右边连续
    public boolean rightContinue(Seat seat){
        for (int i=seat.getColumnnum()+1;i<200;i++){
            Seat s=getSeatByRowAndCol(seat.getRownum(),i) ;
            if(s==null){//右边无位置
                return true;
            }else{
                if(s.isLostSeat()){
                    return false;
                }
            }
        }
        return  true ;
    }

    class SeatNode {
        public Seat pre;
        public Seat seat;
        public Seat next;
    }

    /**
     * 查检左边是否合法
     *
     * @return
     */
    private boolean checkLeft(Seat seat) {
        Seat left = getSeatByRowAndCol(seat.getRownum(),
                seat.getColumnnum() - 1);
        if (left == null) {// 左边为空
            return true;
        }
        if (!allowCanChoiced(seat)) {// 左边为不可选的
            return true;
        }
        if (isChoiced(left)) {// 左边被选择了
            return true;
        } else {// 左边没有被选择接着判断下一个
            Seat left2 = getSeatByRowAndCol(left.getRownum(),
                    left.getColumnnum() - 1);
            if (left2 == null) {
                return false;
            }
            if (isChoiced(left2)) {
                return false;
            }
            return true;
        }
    }

    /**
     * 检查右边是否合法
     *
     * @param seat
     * @return
     */
    private boolean checkRight(Seat seat) {
        Seat r = getSeatByRowAndCol(seat.getRownum(), seat.getColumnnum() + 1);
        if (r == null) {// 右边为空
            return true;
        }
        if (!allowCanChoiced(r)) {// 右边为不可选的
            return true;
        }
        if (isChoiced(r)) {
            return true;
        } else {// 右边可选
            Seat r2 = getSeatByRowAndCol(r.getRownum(), r.getColumnnum() + 1);
            if (r2 == null) {
                return false;
            }
            if (isChoiced(r2)) {
                return false;
            }
            return true;
        }
    }

    private Seat getSeatByRowAndCol(int rowNum, int colNum) {
        for (Seat s : list) {
            if (s.getRownum() == rowNum && s.getColumnnum() == colNum) {
                return s;
            }
        }
        return null;
    }

    public Seat getSeatByRowIdAndColId(String rowId, String colId) {
        for (Seat s : list) {
            if (s.getRowid().equals(rowId) && s.getColumnid().equals(colId)) {
                return s;
            }
        }
        return null;
    }

    /**
     * 座位是否被选择了
     */
    private boolean isChoiced(Seat seat) {
        if (seatView.getChoiceList().contains(seat)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 不可选 损坏或者已经卖出
     *
     * @return 可选为true 不可选 为false
     */

    private boolean allowCanChoiced(Seat seat) {
        if (seat.getIssell().equals("0")) {
            return false;
        }
        if (seat.getDamagedflg().equals("Y")) {
            return false;
        }
        return true;
    }

}
