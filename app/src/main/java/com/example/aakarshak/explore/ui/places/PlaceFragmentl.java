package com.example.aakarshak.explore.ui.places;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.ui.common.SpacingList;
import com.example.aakarshak.explore.utils.ImageUtility;
import com.example.aakarshak.explore.utils.UtilityColor;
import com.example.aakarshak.explore.utils.UtilityIntent;
import com.example.aakarshak.explore.utils.UtlitySnack;
import com.example.aakarshak.explore.workers.DecoderImaFrag;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PlaceFragmentl extends Fragment implements ContractPlaceList.V, SwipeRefreshLayout.OnRefreshListener {

    //Constant used for logs
    private static final String LOG_TAG = PlaceFragmentl.class.getSimpleName();

    //Bundle constants for persisting the data throughout System config changes
    private static final String BUNDLE_PLACES_LIST_KEY = "PlaceList.Places";

    //The PresenterBase interface for this V
    private ContractPlaceList.PresenterBase mPresenter;

    //References to the Views shown in this Fragment
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewContentList;

    //Adapter of the RecyclerView
    private PlaceListAdapter mAdapter;

    public PlaceFragmentl() {
    }

    @NonNull
    public static PlaceFragmentl newInstance() {
        return new PlaceFragmentl();
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
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.placeListBackgroundColor));

        //Returning the prepared layout
        return rootView;
    }

    private void setupSwipeRefresh() {
        //Registering the refresh listener which triggers a new load on swipe to refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //Configuring the Colors for Swipe Refresh Progress Indicator
        mSwipeRefreshLayout.setColorSchemeColors(UtilityColor.obtainColorsFromTypedArray(requireContext(), R.array.swipe_refresh_colors, R.color.colorPrimaryDark));
    }

    private void setupRecyclerView() {
        //Creating a Vertical Linear Layout Manager with the default layout order
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        //Setting the Layout Manager to use
        mRecyclerViewContentList.setLayoutManager(linearLayoutManager);

        //Initializing the Adapter for the RecyclerView
        mAdapter = new PlaceListAdapter(requireContext(), new ItemUserActionPlace());

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

            //Restoring the list of Places
            mPresenter.updatePlaces(savedInstanceState.getParcelableArrayList(BUNDLE_PLACES_LIST_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving the State
        outState.putParcelableArrayList(BUNDLE_PLACES_LIST_KEY, mAdapter.getPlaceList());
    }

    @Override
    public void onResume() {
        super.onResume();

        //Start loading the Places
        mPresenter.start();
    }

    @Override
    public void onRefresh() {
        //Dispatching the event to the PresenterBase to reload the data
        mPresenter.onRefreshMenuClicked();
    }

    @Override
    public void setPresenter(ContractPlaceList.PresenterBase presenter) {
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
    public void loadPlaces(List<PlaceClass> placeClassList) {
        //Submitting the Places data to the Adapter to load
        mAdapter.submitList(placeClassList);
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

    private static class PlaceListAdapter extends ListAdapter<PlaceClass, PlaceListAdapter.ViewHolder> {

        //Payload Constants used to rebind the "Expanded/Collapsed" state of the list items for the position stored here
        private static final String PAYLOAD_EXPAND_CARD = "Payload.Expand.ItemPosition";
        private static final String PAYLOAD_COLLAPSE_CARD = "Payload.Collapse.ItemPosition";

        private static final DiffUtil.ItemCallback<PlaceClass> DIFF_PLACES
                = new DiffUtil.ItemCallback<PlaceClass>() {

            @Override
            public boolean areItemsTheSame(PlaceClass oldItem, PlaceClass newItem) {
                //Returning the comparison of the PlaceClass's Id
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(PlaceClass oldItem, PlaceClass newItem) {
                //Returning the comparison of Name
                return oldItem.getName().equals(newItem.getName());
            }
        };
        //Context for reading resources
        private final Context mContext;
        //Listener for the User actions on the PlaceClass List Items
        private final UserActionPlace mActionsListener;
        //Stores the Item Position of the Last expanded card
        private int mLastExpandedItemPosition = RecyclerView.NO_POSITION;
        //The Data of this Adapter that stores a list of Places to be displayed
        private ArrayList<PlaceClass> mPlaceClassList;

        PlaceListAdapter(Context context, UserActionPlace userActionsListener) {
            super(DIFF_PLACES);
            mContext = context;
            //Registering the User Actions Listener
            mActionsListener = userActionsListener;
        }

        @NonNull
        @Override
        public PlaceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Inflating the item layout 'R.layout.item_place_list'
            //Passing False since we are attaching the layout ourselves
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list, parent, false);
            //Returning the Instance of ViewHolder for the inflated Item V
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceListAdapter.ViewHolder holder, int position) {
            //Get the data at the position
            PlaceClass placeClass = getItem(position);

            //Bind the Views with the data at the position
            holder.bind(position, placeClass);

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
        public void submitList(List<PlaceClass> list) {
            //Preparing the list to store a copy of the Adapter data
            if (mPlaceClassList == null) {
                //Initializing the list when not initialized
                mPlaceClassList = new ArrayList<>();
            } else if (mPlaceClassList.size() > 0) {
                //Clearing the list if it has content already
                mPlaceClassList.clear();
            }
            //Adding all the contents of the list submitted
            mPlaceClassList.addAll(list);
            //Propagating the list to super
            super.submitList(list);
        }

        ArrayList<PlaceClass> getPlaceList() {
            return mPlaceClassList;
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

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            //References to the Views required, in the Item V
            private final TextView mTextViewPlaceName;
            private final TextView mTextViewPlaceTypes;
            private final TextView mTextViewTypesRatingSeparator;
            private final ImageView mImageViewPlaceType;
            private final TextView mTextViewRating;
            private final RatingBar mRatingBar;
            private final ImageView mImageViewPlacePhoto;
            private final TextView mTextViewAccessTime;
            private final TextView mTextViewEntryFee;
            private final TextView mTextViewDescription;
            private final View mViewTopRibbon;
            private final Button mButtonExpandCollapse;
            private final ImageButton mImageButtonLocation;
            private final Group mGroupPlaceLocation;
            private final TextView mTextViewPlaceLocation;

            ViewHolder(View itemView) {
                super(itemView);

                //Finding the Views needed
                mTextViewPlaceName = itemView.findViewById(R.id.text_place_list_item_title);
                mTextViewPlaceTypes = itemView.findViewById(R.id.text_place_list_item_types);
                mTextViewTypesRatingSeparator = itemView.findViewById(R.id.text_place_list_item_types_rating_separator);
                mImageViewPlaceType = itemView.findViewById(R.id.image_place_list_item_type);
                mTextViewRating = itemView.findViewById(R.id.text_place_list_item_rating_value);
                mRatingBar = itemView.findViewById(R.id.ratingbar_place_list_item_rating);
                mImageViewPlacePhoto = itemView.findViewById(R.id.image_place_list_item_photo);
                mTextViewAccessTime = itemView.findViewById(R.id.text_place_list_item_access_time);
                mTextViewEntryFee = itemView.findViewById(R.id.text_place_list_item_entry_fee);
                mTextViewDescription = itemView.findViewById(R.id.text_place_list_item_description);
                mViewTopRibbon = itemView.findViewById(R.id.view_place_list_item_top_ribbon);
                mButtonExpandCollapse = itemView.findViewById(R.id.btn_place_list_item_expand_collapse);
                mImageButtonLocation = itemView.findViewById(R.id.imgbtn_place_list_item_location);
                mGroupPlaceLocation = itemView.findViewById(R.id.group_place_list_item_location);
                mTextViewPlaceLocation = itemView.findViewById(R.id.text_place_list_item_location);

                //Registering the Click listeners on the required views
                mButtonExpandCollapse.setOnClickListener(this);
                mImageButtonLocation.setOnClickListener(this);
                mTextViewPlaceLocation.setOnClickListener(this);
                mTextViewPlaceLocation.setOnLongClickListener(this);
                itemView.setOnClickListener(this);
            }

            void bind(int position, PlaceClass placeClass) {
                //Bind the PlaceClass Name
                mTextViewPlaceName.setText(placeClass.getName());
                //Bind the PlaceClass Types
                mTextViewPlaceTypes.setText(placeClass.getTypes());
                //Bind the PlaceClass Type Image if available
                bindImageView(mImageViewPlaceType, placeClass.getTypeImageResId());
                //Bind the Rating value
                mTextViewRating.setText(String.valueOf(placeClass.getRating()));
                //Bind the RatingBar with the Rating Value
                mRatingBar.setRating(placeClass.getRating());
                //Bind the PlaceClass Photo if available
                DecoderImaFrag.newInstance(((FragmentActivity) mContext).getSupportFragmentManager(), position)
                        .executeAndUpdate(mImageViewPlacePhoto, placeClass.getPhotoResId());
                //Bind the Access Time Info
                mTextViewAccessTime.setText(placeClass.getAccessTimeInfo());
                //Bind the Entry Fee Info
                mTextViewEntryFee.setText(placeClass.getEntryFeeInfo());
                //Bind the Description
                mTextViewDescription.setText(placeClass.getDescription());
                //Bind the Location Info
                mTextViewPlaceLocation.setText(placeClass.getLocation());
                //Bind the Colors using the Palette if derived
                if (placeClass.isSwatchGenerated()) {
                    //Use the Palette Colors when we have them
                    int colorPrimary = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
                    mViewTopRibbon.setBackgroundColor(colorPrimary);

                } else {
                    //When we do not have any Palette, use the default colors
                    int colorPrimary = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
                    mViewTopRibbon.setBackgroundColor(colorPrimary);
                    TextViewCompat.setTextAppearance(mTextViewPlaceName, androidx.appcompat.R.style.TextAppearance_AppCompat_Title_Inverse);
                    TextViewCompat.setTextAppearance(mTextViewPlaceTypes, androidx.appcompat.R.style.TextAppearance_AppCompat_Small_Inverse);
                    TextViewCompat.setTextAppearance(mTextViewTypesRatingSeparator, androidx.appcompat.R.style.TextAppearance_AppCompat_Small_Inverse);
                    TextViewCompat.setTextAppearance(mTextViewRating, androidx.appcompat.R.style.TextAppearance_AppCompat_Small_Inverse);
                    mButtonExpandCollapse.setTextColor(colorPrimary);
                }
            }

            @SuppressLint("ResourceType")
            void bindImageView(ImageView imageView, @DrawableRes int imageResourceId) {
                if (imageResourceId > ImageUtility.NO_RESOURCE_ID) {
                    //When the Image is available

                    //Ensure the ImageView is visible
                    imageView.setVisibility(View.VISIBLE);
                    //Executing on the background thread
                    Executors.newSingleThreadExecutor().execute(() -> {
                        //Decode the Image from the Resource
                        Drawable decodedDrawable = AppCompatResources.getDrawable(mContext, imageResourceId);
                        //Executing on the UI thread
                        ((FragmentActivity) mContext).runOnUiThread(() -> {
                            //Set the Image decoded to the ImageView
                            imageView.setImageDrawable(decodedDrawable);
                        });
                    });
                } else {
                    //Hide the ImageView when the Image is NOT available
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //When the adapter position is valid

                    //Get the data at the position
                    PlaceClass placeClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == itemView.getId()) {
                        //When the entire Item V is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLink(adapterPosition, placeClass);

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
                            || clickedViewId == mTextViewPlaceLocation.getId()) {
                        //When the Location ImageButton or the Location Info is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLocation(adapterPosition, placeClass);
                    }

                }
            }

            @Override
            public boolean onLongClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //Get the data at the position
                    PlaceClass placeClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == mTextViewPlaceLocation.getId()) {
                        //When the Location Info is long pressed

                        //Dispatch the event to the action listener
                        mActionsListener.onLocationLongClicked(adapterPosition, placeClass);

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
                mGroupPlaceLocation.setVisibility(View.VISIBLE);
            }

            void collapseItemView() {
                //Restricting the description to Max 3 Lines
                mTextViewDescription.setMaxLines(mContext.getResources().getInteger(R.integer.place_list_item_description_collapsed_max_lines));
                //Ellipsizing the Text
                mTextViewDescription.setEllipsize(TextUtils.TruncateAt.END);
                //Setting the Button Text to Expand
                mButtonExpandCollapse.setText(R.string.expand_action);

                //Revealing the ImageButton for Location
                mImageButtonLocation.setVisibility(View.VISIBLE);
                //Hiding the Location Info
                mGroupPlaceLocation.setVisibility(View.GONE);
            }


        }
    }

    private class ItemUserActionPlace implements UserActionPlace {

        @Override
        public void onOpenLink(int itemPosition, PlaceClass placeClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLink(placeClass.getWebsite());
        }

        @Override
        public void onOpenLocation(int itemPosition, PlaceClass placeClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLocation(placeClass.getLocation());
        }

        @Override
        public void onLocationLongClicked(int itemPosition, PlaceClass placeClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.shareLocation(placeClass.getLocation());
        }
    }
}
