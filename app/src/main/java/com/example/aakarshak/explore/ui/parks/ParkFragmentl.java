package com.example.aakarshak.explore.ui.parks;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.ui.common.SpacingList;
import com.example.aakarshak.explore.utils.UtilityColor;
import com.example.aakarshak.explore.utils.UtilityIntent;
import com.example.aakarshak.explore.utils.UtlitySnack;
import com.example.aakarshak.explore.workers.DecoderImaFrag;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class ParkFragmentl extends Fragment
        implements ContractPlist.V, SwipeRefreshLayout.OnRefreshListener {

    //Constant used for logs
    private static final String LOG_TAG = ParkFragmentl.class.getSimpleName();

    //Bundle constants for persisting the data throughout System config changes
    private static final String BUNDLE_PARKS_LIST_KEY = "ParkList.Parks";

    //The PresenterBase interface for this V
    private ContractPlist.PresenterBase mPresenter;

    //References to the Views shown in this Fragment
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewContentList;

    //Adapter of the RecyclerView
    private ParkListAdapter mAdapter;

    public ParkFragmentl() {
    }

    public static ParkFragmentl newInstance() {
        return new ParkFragmentl();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout 'R.layout.layout_main_content_page'
        //Passing false as we are attaching the layout ourselves
        View rootView = inflater.inflate(R.layout.main_content_page, container, false);

        //Finding the Views
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_content_page);
        mRecyclerViewContentList = rootView.findViewById(R.id.recyclerview_content_page);

        //Initialize SwipeRefreshLayout
        setupSwipeRefresh();

        //Initialize RecyclerView
        setupRecyclerView();

        //Setting the background color for the root view
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.parkListBackgroundColor));

        //Returning the prepared layout
        return rootView;
    }


    private void setupSwipeRefresh() {
        //Registering the refresh listener which triggers a new load on swipe to refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //Configuring the Colors for Swipe Refresh Progress Indicator
        mSwipeRefreshLayout.setColorSchemeColors(UtilityColor.obtainColorsFromTypedArray(requireContext(), R.array.swipe_refresh_colors, R.color.colorPrimary));
    }

    private void setupRecyclerView() {
        //Creating a Vertical Linear Layout Manager with the default layout order
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        //Setting the Layout Manager to use
        mRecyclerViewContentList.setLayoutManager(linearLayoutManager);

        //Initializing the Adapter for the RecyclerView
        mAdapter = new ParkListAdapter(requireContext(), new ItemUserActionPark());

        //Setting the Adapter for RecyclerView
        mRecyclerViewContentList.setAdapter(mAdapter);

        //Retrieving the Item spacing to use
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.all_list_items_spacing);

        //Setting Item offsets using Item Decoration
        mRecyclerViewContentList.addItemDecoration(new SpacingList(itemSpacing, itemSpacing));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //On Subsequent launch

            //Restoring the list of Parks
            mPresenter.updateParks(savedInstanceState.getParcelableArrayList(BUNDLE_PARKS_LIST_KEY));
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving the State
        outState.putParcelableArrayList(BUNDLE_PARKS_LIST_KEY, mAdapter.getParkList());
    }

    @Override
    public void onResume() {
        super.onResume();

        //Start loading the Parks
        mPresenter.start();
    }


    @Override
    public void onRefresh() {
        //Dispatching the event to the PresenterBase to reload the data
        mPresenter.onRefreshMenuClicked();
    }

    @Override
    public void setPresenter(ContractPlist.PresenterBase presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressIndicator() {
        //Enabling the Swipe to Refresh if disabled prior to showing the Progress indicator
        if (!mSwipeRefreshLayout.isEnabled()) {
            mSwipeRefreshLayout.setEnabled(true);
        }
        //Displaying the Progress Indicator only when not already shown
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgressIndicator() {
        //Hiding the Progress indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadParks(List<ParkClass> parkClassList) {
        //Submitting the Parks data to the Adapter to load
        mAdapter.submitList(parkClassList);
    }

    @Override
    public void showError(int messageId, @Nullable Object... args) {
        if (getView() != null) {
            //When we have the root view

            //Evaluating the message to be shown
            String messageToBeShown;
            if (args != null && args.length > 0) {
                //For the String Resource with args
                messageToBeShown = getString(messageId, args);
            } else {
                //For the String Resource without args
                messageToBeShown = getString(messageId);
            }

            if (!TextUtils.isEmpty(messageToBeShown)) {
                //Displaying the Snackbar message of indefinite time length
                //when we have the error message to be shown

                new UtlitySnack(Snackbar.make(getView(), messageToBeShown, Snackbar.LENGTH_INDEFINITE))
                        .revealCompleteMessage() //Removes the limit on max lines
                        .setDismissAction(R.string.action_snackbar) //For the Dismiss "OK" action
                        .showSnack();
            }
        }
    }

    @Override
    public void showNoLinkError() {
        showError(R.string.no_link_error);
    }

    @Override
    public void launchWebLink(String webUrl) {
        //Launching the Browser application for the Web Page URL passed
        UtilityIntent.openLink(requireContext(), webUrl);
    }

    @Override
    public void launchMapLocation(String location) {
        //Launching the Map application for the location information passed
        UtilityIntent.openMap(requireContext(), location);
    }

    @Override
    public void launchShareLocation(String location) {
        //Launching the Share Intent to share the location text
        UtilityIntent.shareText(requireActivity(), location, getString(R.string.all_location_title));
    }

    private static class ParkListAdapter extends ListAdapter<ParkClass, ParkListAdapter.ViewHolder> {

        //Payload Constants used to rebind the "Expanded/Collapsed" state of the list items for the position stored here
        private static final String PAYLOAD_EXPAND_CARD = "Payload.Expand.ItemPosition";
        private static final String PAYLOAD_COLLAPSE_CARD = "Payload.Collapse.ItemPosition";

        private static final DiffUtil.ItemCallback<ParkClass> DIFF_PARKS
                = new DiffUtil.ItemCallback<ParkClass>() {

            @Override
            public boolean areItemsTheSame(ParkClass oldItem, ParkClass newItem) {
                //Returning the comparison of the ParkClass's Id
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(ParkClass oldItem, ParkClass newItem) {
                //Returning the comparison of Name
                return oldItem.getName().equals(newItem.getName());
            }
        };
        //Context for reading resources
        private final Context mContext;
        //Listener for the User actions on the ParkClass List Items
        private final UserActionPark mActionsListener;
        //Stores the Item Position of the Last expanded card
        private int mLastExpandedItemPosition = RecyclerView.NO_POSITION;
        //The Data of this Adapter that stores a list of Parks to be displayed
        private ArrayList<ParkClass> mParkClassList;


        ParkListAdapter(Context context, UserActionPark userActionsListener) {
            super(DIFF_PARKS);
            mContext = context;
            //Registering the User Actions Listener
            mActionsListener = userActionsListener;
        }

        @NonNull
        @Override
        public ParkListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Inflating the item layout 'R.layout.item_park_list'
            //Passing False since we are attaching the layout ourselves
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_list, parent, false);
            //Returning the Instance of ViewHolder for the inflated Item V
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ParkListAdapter.ViewHolder holder, int position) {
            //Get the data at the position
            ParkClass parkClass = getItem(position);

            //Bind the Views with the data at the position
            holder.bind(position, parkClass);

            //When we have an Item V that was last expanded
            if (mLastExpandedItemPosition > RecyclerView.NO_POSITION) {
                if (mLastExpandedItemPosition == position) {
                    //Ensures that the Item V remains expanded when being reused
                    holder.expandItemView();
                } else {
                    //Ensures that the Item V remains collapsed when being reused
                    holder.collapseItemView();
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
            if (payloads.isEmpty()) {
                //Propagating to super when there are no payloads
                super.onBindViewHolder(holder, position, payloads);
            } else {
                //When we have a payload for partial update

                //Get the Payload bundle
                Bundle bundle = (Bundle) payloads.get(0);
                //Iterate over the bundle keys
                for (String keyStr : bundle.keySet()) {
                    switch (keyStr) {
                        case PAYLOAD_EXPAND_CARD:
                            //For the Item to be expanded

                            //Get the position from the bundle
                            int itemPositionToExpand = bundle.getInt(keyStr, RecyclerView.NO_POSITION);
                            if (itemPositionToExpand > RecyclerView.NO_POSITION
                                    && itemPositionToExpand == mLastExpandedItemPosition) {
                                //When the position is for the Item to be expanded, expand the Item V
                                holder.expandItemView();
                            }
                            break;
                        case PAYLOAD_COLLAPSE_CARD:
                            //For the Item to be collapsed

                            //Get the position from the bundle
                            int itemPositionToCollapse = bundle.getInt(keyStr, RecyclerView.NO_POSITION);
                            if (itemPositionToCollapse > RecyclerView.NO_POSITION) {
                                //When the Item V Position is valid, collapse the Item V
                                holder.collapseItemView();
                            }
                            break;
                    }
                }
            }
        }

        @Override
        public void submitList(List<ParkClass> list) {
            //Preparing the list to store a copy of the Adapter data
            if (mParkClassList == null) {
                //Initializing the list when not initialized
                mParkClassList = new ArrayList<>();
            } else if (mParkClassList.size() > 0) {
                //Clearing the list if it has content already
                mParkClassList.clear();
            }
            //Adding all the contents of the list submitted
            mParkClassList.addAll(list);
            //Propagating the list to super
            super.submitList(list);
        }

        ArrayList<ParkClass> getParkList() {
            return mParkClassList;
        }

        void changeItemExpanded(int position) {
            //Collapse any previously expanded card
            if (mLastExpandedItemPosition > RecyclerView.NO_POSITION) {
                setItemCollapsed(mLastExpandedItemPosition);
            }
            //Creating a Bundle to do a partial update for expanding the card
            Bundle payloadBundle = new Bundle(1);
            //Put the position of the item into the bundle for update
            payloadBundle.putInt(PAYLOAD_EXPAND_CARD, position);
            //Store the position of the item being expanded for tracking
            mLastExpandedItemPosition = position;
            //Notify the state change at the item position, to expandItemView
            notifyItemChanged(position, payloadBundle);
        }

        void setItemCollapsed(int position) {
            //Creating a Bundle to do a partial update for collapsing the card
            Bundle payloadBundle = new Bundle(1);
            //Put the position of the item into the bundle for update
            payloadBundle.putInt(PAYLOAD_COLLAPSE_CARD, position);
            //Notify the state change at the item position, to collapse
            notifyItemChanged(position, payloadBundle);
            //Reset the last expanded position if the same item is being collapsed
            if (mLastExpandedItemPosition == position) {
                mLastExpandedItemPosition = RecyclerView.NO_POSITION;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnLongClickListener {
            //References to the Views required, in the Item V
            private final TextView mTextViewParkName;
            private final TextView mTextViewRating;
            private final RatingBar mRatingBar;
            private final ImageView mImageViewParkPhoto;
            private final TextView mTextViewAccessTime;
            private final TextView mTextViewEntryFee;
            private final TextView mTextViewDescription;
            private final Button mButtonExpandCollapse;
            private final ImageButton mImageButtonLocation;
            private final Group mGroupParkLocation;
            private final TextView mTextViewParkLocation;

            ViewHolder(View itemView) {
                super(itemView);

                //Finding the Views needed
                mTextViewParkName = itemView.findViewById(R.id.text_park_list_item_title);
                mTextViewRating = itemView.findViewById(R.id.text_park_list_item_rating_value);
                mRatingBar = itemView.findViewById(R.id.ratingbar_park_list_item_rating);
                mImageViewParkPhoto = itemView.findViewById(R.id.image_park_list_item_photo);
                mTextViewAccessTime = itemView.findViewById(R.id.text_park_list_item_access_time);
                mTextViewEntryFee = itemView.findViewById(R.id.text_park_list_item_entry_fee);
                mTextViewDescription = itemView.findViewById(R.id.text_park_list_item_description);
                mButtonExpandCollapse = itemView.findViewById(R.id.btn_park_list_item_expand_collapse);
                mImageButtonLocation = itemView.findViewById(R.id.imgbtn_park_list_item_location);
                mGroupParkLocation = itemView.findViewById(R.id.group_park_list_item_location);
                mTextViewParkLocation = itemView.findViewById(R.id.text_park_list_item_location);

                //Registering the Click listeners on the required views
                mButtonExpandCollapse.setOnClickListener(this);
                mImageButtonLocation.setOnClickListener(this);
                mTextViewParkLocation.setOnClickListener(this);
                mTextViewParkLocation.setOnLongClickListener(this);
                itemView.setOnClickListener(this);
            }

            void bind(int position, ParkClass parkClass) {
                //Bind the ParkClass Name
                mTextViewParkName.setText(parkClass.getName());
                //Bind the Rating value
                mTextViewRating.setText(String.valueOf(parkClass.getRating()));
                //Bind the RatingBar with the Rating Value
                mRatingBar.setRating(parkClass.getRating());
                //Bind the ParkClass Photo if available
                DecoderImaFrag.newInstance(((FragmentActivity) mContext).getSupportFragmentManager(), position)
                        .executeAndUpdate(mImageViewParkPhoto, parkClass.getPhotoResId());
                //Bind the Access Time Info
                mTextViewAccessTime.setText(parkClass.getAccessTimeInfo());
                //Bind the Entry Fee Info
                mTextViewEntryFee.setText(parkClass.getEntryFeeInfo());
                //Bind the Description
                mTextViewDescription.setText(parkClass.getDescription());
                //Bind the Location Info
                mTextViewParkLocation.setText(parkClass.getLocation());
                //Bind the Colors using the Palette if derived
                if (parkClass.isSwatchGenerated()) {
                    //Use the Palette Colors when we have them
                    mButtonExpandCollapse.setTextColor(parkClass.getSwatchRgbColor());
                } else {
                    //When we do not have any Palette, use the default colors
                    mButtonExpandCollapse.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                }
            }

            @Override
            public void onClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //When the adapter position is valid

                    //Get the data at the position
                    ParkClass parkClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == itemView.getId()) {
                        //When the entire Item V is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLink(adapterPosition, parkClass);

                    } else if (clickedViewId == mButtonExpandCollapse.getId()) {
                        //When the "Expand/Collapse" button is clicked

                        if (mLastExpandedItemPosition == adapterPosition) {
                            //When the same Item V was previously expanded, collapse the Item V
                            setItemCollapsed(adapterPosition);
                        } else {
                            //Else, expand the Item V
                            changeItemExpanded(adapterPosition);
                        }

                    } else if (clickedViewId == mImageButtonLocation.getId()
                            || clickedViewId == mTextViewParkLocation.getId()) {
                        //When the Location ImageButton or the Location Info is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLocation(adapterPosition, parkClass);
                    }

                }
            }

            @Override
            public boolean onLongClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //Get the data at the position
                    ParkClass parkClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == mTextViewParkLocation.getId()) {
                        //When the Location Info is long pressed

                        //Dispatch the event to the action listener
                        mActionsListener.onLocationLongClicked(adapterPosition, parkClass);

                        //Returning True to indicate the event was consumed
                        return true;
                    }
                }
                //Returning False when no event was consumed
                return false;
            }

            void expandItemView() {
                //Revealing the Complete description
                mTextViewDescription.setMaxLines(Integer.MAX_VALUE);
                //Clearing the Ellipse
                mTextViewDescription.setEllipsize(null);
                //Setting the Button Text to Collapse
                mButtonExpandCollapse.setText(R.string.collapse_action);

                //Hiding the ImageButton for Location
                mImageButtonLocation.setVisibility(View.GONE);
                //Revealing the Location Info
                mGroupParkLocation.setVisibility(View.VISIBLE);
            }

            void collapseItemView() {
                //Restricting the description to Max 3 Lines
                mTextViewDescription.setMaxLines(mContext.getResources().getInteger(R.integer.park_list_item_description_collapsed_max_lines));
                //Ellipsizing the Text
                mTextViewDescription.setEllipsize(TextUtils.TruncateAt.END);
                //Setting the Button Text to Expand
                mButtonExpandCollapse.setText(R.string.expand_action);

                //Revealing the ImageButton for Location
                mImageButtonLocation.setVisibility(View.VISIBLE);
                //Hiding the Location Info
                mGroupParkLocation.setVisibility(View.GONE);
            }

        }
    }

    private class ItemUserActionPark implements UserActionPark {

        @Override
        public void onOpenLink(int itemPosition, ParkClass parkClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLink(parkClass.getWebsite());
        }

        @Override
        public void onOpenLocation(int itemPosition, ParkClass parkClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLocation(parkClass.getLocation());
        }

        @Override
        public void onLocationLongClicked(int itemPosition, ParkClass parkClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.shareLocation(parkClass.getLocation());
        }
    }
}
