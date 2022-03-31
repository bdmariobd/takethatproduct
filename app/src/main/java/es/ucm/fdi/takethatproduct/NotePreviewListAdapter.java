package es.ucm.fdi.takethatproduct;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.takethatproduct.integration.Note;

public class NotePreviewListAdapter extends RecyclerView.Adapter<NotePreviewListAdapter.NoteViewHolder> {


    private List<Note> mNoteList;
    private final LayoutInflater mInflater;
    private final MainActivity context;

    public void setmNoteList(List<Note> mNoteList){
        this.mNoteList = mNoteList;
        notifyDataSetChanged();
    }

    public NotePreviewListAdapter(MainActivity context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        final NotePreviewListAdapter mAdapter;
        public final TextView noteTotalViewTitle;
        public final EditText noteTotalViewBody;
        public final View itemView;
        public NoteViewHolder(View itemView, NotePreviewListAdapter adapter) {
            super(itemView);
            noteTotalViewTitle = itemView.findViewById(R.id.noteTotalViewTitle);
            noteTotalViewBody = itemView.findViewById(R.id.noteTotalViewBody);
            this.mAdapter = adapter;
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public NotePreviewListAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.note_total_view, parent, false);

        return new NoteViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NotePreviewListAdapter.NoteViewHolder holder, int position) {
        //code to set one note preview content
        holder.noteTotalViewTitle.setText("Prueba");
        holder.noteTotalViewBody.setText("Prueba prueba loren ipsum la xavineta xdd");
        /*
        BookInfo book = mBookList.get(position);
        holder.bookTitle.setText(book.getTitle());
        //https://stackoverflow.com/questions/2394935/can-i-underline-text-in-an-android-layout
        holder.bookTitle.setPaintFlags(holder.bookTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.bookAuthor.setText(book.getAuthors());
        holder.bookTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(book.getInfoLink().toString()));
                context.startActivity(browserIntent);
            }
        });
        //https://github.com/bumptech/glide
        Glide.with(context)
                .load(book.getImage().toString())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.bookImage);


         */
    }

    @Override
    public int getItemCount() {
        if(mNoteList == null) return 0;
        return this.mNoteList.size();
    }
}
