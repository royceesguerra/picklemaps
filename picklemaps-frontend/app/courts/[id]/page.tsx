'use client';

import Image from 'next/image';
import Link from 'next/link';
import {
  ChevronLeft,
  Dot,
  Star,
  MessageSquare,
  ArrowUpDown,
  Edit,
  PenLine,
  Trash2,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useEffect, useState } from 'react';
import { Court, CourtSummary, Review } from '@/domain/domain';
import { useAppContext } from '@/providers/app-context-provider';
import OpenStreetMap from '@/components/open-street-maps';
import { useRouter } from 'next/navigation';
import { useAuth } from 'react-oidc-context';
import CourtList from '@/components/court-list';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

export default function CourtPage({ params }: { params: { id: string } }) {
  const { apiService } = useAppContext();
  const { isAuthenticated, signinRedirect } = useAuth();
  const router = useRouter();

  const [loading, setLoading] = useState(true);
  const [court, setCourt] = useState<Court | null>(null);
  const [courtsNear, setCourtsNear] = useState<CourtSummary[] | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [sortOrder, setSortOrder] = useState<
    'datePosted,desc' | 'datePosted,asc' | 'rating,desc' | 'rating,asc'
  >('datePosted,desc');

  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deleteType, setDeleteType] = useState<'court' | 'review'>('court');
  const [reviewToDelete, setReviewToDelete] = useState<Review | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (null == apiService) {
          throw Error('API Service not available!');
        }
        const court = await apiService.getCourt(params.id);
        setCourt(court);

        const courtsNearBy = await apiService.searchCourts({
          latitude: court.geoLocation?.latitude,
          longitude: court.geoLocation?.longitude,
          radius: 50,
        });

        const filteredCourts = courtsNearBy.content.filter(
          nearbyCourt => nearbyCourt.id !== court.id,
        );
        setCourtsNear(filteredCourts);

        if (court.reviews) {
          setReviews(court.reviews);
        }
        setLoading(false);
      } catch (error) {
        console.error('Error fetching court data:', error);
        setLoading(false);
      }
    };
    fetchData();
  }, [params.id, apiService]);

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        if (apiService && court) {
          const reviewsResponse = await apiService.getCourtReviews(params.id, sortOrder);
          if (reviewsResponse && Array.isArray(reviewsResponse.content)) {
            setReviews(reviewsResponse.content);
          } else {
            console.error('Unexpected reviews response format:', reviewsResponse);
            setReviews(court.reviews || []);
          }
        }
      } catch (error) {
        console.error('Error fetching reviews:', error);
        if (court?.reviews) {
          setReviews(court.reviews);
        }
      }
    };
    fetchReviews();
  }, [sortOrder, params.id, court, apiService]);

  const getImageUrl = (court: Court) => {
    if (null != court.photos && court.photos.length > 0 && null != court.photos[0].url) {
      return `/api/photos/${court.photos[0].url}`;
    }
    return null;
  };

  const handleWriteAReview = async (court: Court) => {
    if (isAuthenticated) {
      router.push(`/courts/${court.id}/review`);
    } else {
      await signinRedirect();
    }
  };

  const handleEditCourt = (courtId: string) => {
    router.push(`/courts/update?id=${courtId}`);
  };

  const handleDeleteCourt = async () => {
    if (court) {
      await apiService?.deleteCourt(court.id);
      router.push('/');
    }
  };

  const handleDeleteReview = async () => {
    if (court && reviewToDelete) {
      try {
        await apiService?.deleteReview(court.id, reviewToDelete.id);
        const updatedReviews = reviews.filter(review => review.id !== reviewToDelete.id);
        setReviews(updatedReviews);
      } catch (error) {
        console.error('Error deleting review:', error);
        alert('Failed to delete review. Please try again.');
      } finally {
        setDeleteModalOpen(false);
        setReviewToDelete(null);
      }
    }
  };

  const openDeleteModal = (type: 'court' | 'review', review?: Review) => {
    setDeleteType(type);
    if (type === 'review' && review) {
      setReviewToDelete(review);
    }
    setDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (deleteType === 'court') {
      await handleDeleteCourt();
    } else {
      await handleDeleteReview();
    }
    setDeleteModalOpen(false);
  };

  const handleEditReview = (review: Review) => {
    router.push(`/courts/${params.id}/review?reviewId=${review.id}`);
  };

  const toggleDateSort = () => {
    setSortOrder(current =>
      current.startsWith('datePosted')
        ? current === 'datePosted,desc'
          ? 'datePosted,asc'
          : 'datePosted,desc'
        : 'datePosted,desc',
    );
  };

  const toggleRatingSort = () => {
    setSortOrder(current =>
      current.startsWith('rating')
        ? current === 'rating,desc'
          ? 'rating,asc'
          : 'rating,desc'
        : 'rating,desc',
    );
  };

  const getSortLabel = () => {
    switch (sortOrder) {
      case 'datePosted,desc':
        return 'Newest first';
      case 'datePosted,asc':
        return 'Oldest first';
      case 'rating,desc':
        return 'Highest rating';
      case 'rating,asc':
        return 'Lowest rating';
      default:
        return 'Sort';
    }
  };

  if (loading || null == court) {
    return (
      <div className="max-w-[1200px] mx-auto px-4 py-8">
        <p>Loading...</p>
      </div>
    );
  }

  if (null != court) {
    return (
      <div className="max-w-[1200px] mx-auto px-4 py-8">
        <div className="flex mb-10 opacity-[0.6] text-[14px]">
          <ChevronLeft size={20} />
          <Link href={'/'}>Back</Link>
        </div>
        <div className="grid md:grid-cols-2 gap-8 mb-8">
          <div>
            <div className="flex justify-between">
              <h1 className="text-4xl font-bold mb-2">{court.name}</h1>
              {isAuthenticated && (
                <div className="text-black/60 cursor-pointer flex gap-4">
                  {/* Edit */}
                  <div onClick={() => handleEditCourt(court.id)}>
                    <PenLine />
                  </div>
                  {/* Delete */}
                  <div onClick={() => openDeleteModal('court')}>
                    <Trash2 />
                  </div>
                </div>
              )}
            </div>

            <p className="text-muted-foreground mb-2 flex">
              {court.address.city}, {court.address.province} <Dot /> {court.courtType} Court <Dot />{' '}
              {court.surfaceType} Surface <Dot /> {court.contactInformation}
            </p>
            <div className="flex items-center gap-2 mb-4">
              <div className="flex">
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star
                    key={i}
                    className={`w-5 h-5 ${
                      i < court.averageRating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'
                    }`}
                  />
                ))}
              </div>
              <span className="text-muted-foreground">({court?.totalReviews || 0} reviews)</span>
            </div>
            <Button
              size="lg"
              className="w-full md:w-auto"
              onClick={() => handleWriteAReview(court)}
            >
              Write a review
            </Button>
            <div className="mt-4">
              {court.geoLocation && <OpenStreetMap location={court.geoLocation} />}
            </div>
          </div>
          <div className="aspect-[4/3] relative">
            <Image
              src={getImageUrl(court) || '/placeholder.svg'}
              alt={court.name}
              fill
              className="object-cover rounded-lg"
            />
          </div>
        </div>

        {courtsNear && (
          <div className="space-y-6 mb-16">
            <h2 className="text-2xl font-semibold">Courts Nearby</h2>
            <CourtList loading={loading} courts={courtsNear?.slice(0, 4)} />
          </div>
        )}

        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-semibold">Reviews</h2>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" size="sm" className="flex items-center gap-2">
                  <ArrowUpDown className="h-4 w-4" />
                  {getSortLabel()}
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem onClick={toggleDateSort}>
                  {sortOrder === 'datePosted,desc' ? 'Oldest first' : 'Newest first'}
                </DropdownMenuItem>
                <DropdownMenuItem onClick={toggleRatingSort}>
                  {sortOrder === 'rating,desc' ? 'Lowest rating' : 'Highest rating'}
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>

          {reviews && reviews.length > 0 ? (
            reviews.map(review => (
              <div key={review.id} className="border-b pb-6">
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center gap-4">
                    <div className="flex gap-2">
                      <p className="text-sm">{review.writtenBy.username}</p>
                      <p className="text-sm text-muted-foreground">
                        {new Date(review.datePosted).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                  {isAuthenticated && (
                    <div className="flex gap-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleEditReview(review)}
                        className="flex items-center gap-2"
                      >
                        <Edit className="h-4 w-4" />
                        Edit
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => openDeleteModal('review', review)}
                        className="flex items-center gap-2"
                      >
                        <Trash2 className="h-4 w-4" />
                        Delete
                      </Button>
                    </div>
                  )}
                </div>
                <div className="flex mb-2">
                  {Array.from({ length: 5 }).map((_, i) => (
                    <Star
                      key={i}
                      className={`w-4 h-4 ${
                        i < review.rating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'
                      }`}
                    />
                  ))}
                </div>
                <p className="text-gray-600">{review.content}</p>
                {review.photos && review.photos.length > 0 && (
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
                    {review.photos
                      .filter(photo => null !== photo.url)
                      .map(photo => (
                        <div key={photo.id} className="relative aspect-square">
                          <Image
                            src={`/api/photos/${photo.url}`}
                            alt={photo.caption || 'Review photo'}
                            fill
                            className="object-cover rounded-lg"
                          />
                        </div>
                      ))}
                  </div>
                )}
              </div>
            ))
          ) : (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <MessageSquare className="w-12 h-12 text-gray-400 mb-4" />
              <p className="text-lg text-gray-600 mb-2">No reviews yet</p>
            </div>
          )}
        </div>

        {/* Shared Delete Modal */}
        <AlertDialog open={deleteModalOpen} onOpenChange={setDeleteModalOpen}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>
                Are you sure you want to delete this {deleteType}?
              </AlertDialogTitle>
              <AlertDialogDescription>
                This action cannot be undone. This will permanently delete the {deleteType}!
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancel</AlertDialogCancel>
              <AlertDialogAction onClick={handleConfirmDelete}>Delete</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    );
  }

  return <p>Error</p>;
}
